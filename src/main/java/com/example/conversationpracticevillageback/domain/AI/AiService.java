package com.example.conversationpracticevillageback.domain.AI;

import com.example.conversationpracticevillageback.domain.entity.Persona;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException.BadRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound;
import org.springframework.web.reactive.function.client.WebClientResponseException.InternalServerError;
import org.springframework.web.reactive.function.client.WebClientResponseException.ServiceUnavailable;
import org.springframework.web.reactive.function.client.WebClientResponseException.Unauthorized;
import org.springframework.web.reactive.function.client.WebClientResponseException.Forbidden;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final ObjectMapper objectMapper;

    @Value("${google.gemini.api-key}")
    private String apiKey;

    @Value("${google.gemini.base-url}")
    private String geminiBaseUrl;

    @Value("${google.gemini.api-version:v1beta}")
    private String geminiApiVersion;

    @Value("${google.gemini.models:gemini-1.5-flash-latest,gemini-1.5-flash}")
    private String geminiModels;

    @jakarta.annotation.PostConstruct
    public void init() {
        log.info("=== Gemini API 설정 초기화 ===");
        log.info("Base URL: {}", geminiBaseUrl);
        log.info("API Version: {}", geminiApiVersion);
        log.info("모델 설정: {}", geminiModels);

        if (apiKey == null || apiKey.isEmpty()) {
            log.error("❌ GEMINI_API_KEY가 null이거나 비어있습니다.");
            return;
        }

        if (apiKey.equals("${GEMINI_API_KEY}")) {
            log.error("❌ GEMINI_API_KEY가 환경변수로 치환되지 않았습니다. 환경변수를 확인하세요.");
            return;
        }

        log.info("✅ API 키 로드 완료 (길이: {})", apiKey.length());
        log.info("✅ Gemini API 초기화 완료. 사용 가능 모델: {}", geminiModels);

        // 사용 가능한 모델 목록 조회
        try {
            listAvailableModels();
        } catch (Exception e) {
            log.warn("사용 가능한 모델 목록 조회 실패: {}", e.getMessage());
        }
    }

    // 사용 가능한 모델 목록을 API에서 조회
    private void listAvailableModels() throws Exception {
        String listModelsUrl = String.format("%s/%s/models?key=%s",
                geminiBaseUrl, geminiApiVersion, apiKey);

        log.info("ListModels API 호출: {}", listModelsUrl.replace(apiKey, "***"));

        RestClient restClient = RestClient.create();
        String response = restClient.get()
                .uri(listModelsUrl)
                .retrieve()
                .body(String.class);

        JsonNode root = objectMapper.readTree(response);
        JsonNode models = root.path("models");

        log.info("=== 사용 가능한 Gemini 모델 목록 ===");
        if (models.isArray()) {
            for (JsonNode model : models) {
                String modelName = model.path("name").asText();
                String displayName = model.path("displayName").asText();
                String description = model.path("description").asText();
                log.info("모델: {} ({})", modelName, displayName);
                if (!description.isBlank()) {
                    log.info("  설명: {}", description.substring(0, Math.min(100, description.length())));
                }
            }
        }
    }

    // 페르소나 설정에 맞춰 AI 답변 생성
    public String generateReply(Persona persona, String userMessage) {
        String prompt = buildPrompt(persona, userMessage);
        List<String> models = Arrays.stream(geminiModels.split(","))
                .map(String::trim)
                .filter(m -> !m.isEmpty())
                .toList();

        Exception lastException = null;
        for (String model : models) {
            try {
                log.info("Gemini API 호출 시도: model={}", model);
                return callGemini(model, prompt);
            } catch (HttpClientErrorException e) {
                // HTTP 에러 (404, 400 등) - 다음 모델로 fallback
                log.warn("Gemini model HTTP 에러: {} (상태: {}). 응답: {}. 다음 모델로 재시도합니다.",
                        model, e.getStatusCode(), e.getResponseBodyAsString());
                lastException = e;
            } catch (Exception e) {
                log.error("Gemini 호출 중 예외 발생(model={}): 상세 에러:", model);
                log.error("에러 메시지: {}", e.getMessage());
                log.error("상세 스택트레이스: ", e);
                lastException = e;
            }
        }

        log.error("설정된 Gemini 모델 모두 실패했습니다. models={}, 마지막 에러: {}",
                models, lastException != null ? lastException.getMessage() : "unknown");
        return "지금은 대화할 수 있는 모델을 찾지 못했어. 잠깐 후에 다시 말 걸어줘.";
    }

    // 페르소나 정보를 바탕으로 시스템 프롬프트 조립
    private String buildPrompt(Persona persona, String message) {
        return String.format(
                "너는 지금부터 '%s'이야. 나이는 %d살이고 직업은 %s이야. 성격은 %s(MBTI) 스타일이지. " +
                        "나와는 '%s' 관계야. 말투는 반드시 '%s' 느낌으로 대답해야 돼. " +
                        "절대로 너가 AI라고 말하지 말고, 주어진 역할에 완전히 몰입해서 대화해줘.\n" +
                        "사용자: %s\n너:",
                persona.getName(), persona.getAge(), persona.getJob(),
                persona.getMbti(), persona.getRelationshipType(),
                persona.getSpeechStyle(), message
        );
    }

    // RestClient를 사용하여 실제 Gemini API 호출
    private String callGemini(String model, String prompt) throws Exception {
        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        String endpoint = String.format("%s/%s/models/%s:generateContent?key=%s",
                geminiBaseUrl, geminiApiVersion, model, apiKey);

        log.debug("Gemini API 요청 URL: {}", endpoint.replace(apiKey, "***")); // API 키는 숨김
        log.debug("요청 바디: {}", objectMapper.writeValueAsString(body).substring(0, Math.min(200, body.toString().length())));

        RestClient restClient = RestClient.create();
        String response = restClient.post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(String.class);

        log.debug("Gemini API 응답: {}", response.substring(0, Math.min(500, response.length())));

        JsonNode root = objectMapper.readTree(response);
        JsonNode textNode = root.path("candidates").path(0)
                .path("content").path("parts").path(0)
                .path("text");

        if (textNode.isMissingNode() || textNode.asText().isBlank()) {
            log.error("Gemini 응답 파싱 실패. 전체 응답: {}", response);
            throw new IllegalStateException("Gemini 응답에서 text를 찾을 수 없습니다: " + response);
        }

        String aiText = textNode.asText();
        log.info("Gemini 응답 성공(model={}): {}", model, aiText);
        return aiText;
    }
}