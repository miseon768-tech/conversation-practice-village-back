package domain.AI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.entity.Persona;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final ObjectMapper objectMapper;

    @Value("${google.gemini.api-key}")
    private String apiKey;

    @Value("${google.gemini.url}")
    private String geminiUrl;


// 페르소나 설정에 맞춰 AI 답변 생성
    public String generateReply(Persona persona, String userMessage) {
        String prompt = buildPrompt(persona, userMessage);
        return callGemini(prompt);
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
    private String callGemini(String prompt) {
        try {
            // Gemini API JSON 구조 생성 (contents -> parts -> text)
            Map<String, Object> body = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", prompt)
                            ))
                    )
            );

            RestClient restClient = RestClient.create();

            String response = restClient.post()
                    .uri(geminiUrl + "?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            // 응답 JSON에서 텍스트 데이터 추출
            JsonNode root = objectMapper.readTree(response);
            String aiText = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            log.info("Gemini 응답 성공: {}", aiText);
            return aiText;

        } catch (Exception e) {
            log.error("Gemini 호출 중 에러 발생: ", e);
            return "음... 갑자기 마을에 통신 장애가 생겼나 봐. 잠시 후에 다시 말해줄래?";
        }
    }
}