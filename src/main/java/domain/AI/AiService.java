package domain.AI;

import domain.entity.Persona;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    public String generateReply(Persona persona, String userMessage) {

        String prompt = buildPrompt(persona, userMessage);

        // 여기서 openai / gemini api 호출
        // restTemplate or webClient 사용

        return callExternalAi(prompt);
    }

    private String buildPrompt(Persona persona, String message) {
        return "you are " + persona.getName() +
                ", age " + persona.getAge() +
                ", job " + persona.getJob() +
                ". personality: " + persona.getMbti() +
                ". reply naturally to: " + message;
    }

    private String callExternalAi(String prompt) {
        // 실제 api 호출 부분
        return "ai response sample";
    }
}