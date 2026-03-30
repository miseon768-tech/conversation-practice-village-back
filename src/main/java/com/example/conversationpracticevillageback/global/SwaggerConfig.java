package com.example.conversationpracticevillageback.global;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

@Configuration
public class SwaggerConfig {

    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Conversation Practice Village API")
                        .version("1.0.0")
                        .description("AI 기반 대화 연습 게임 - NPC와의 실시간 대화를 통한 언어 학습 플랫폼")
                        .contact(new Contact()
                                .name("Conversation Practice Village Team")
                                .url("https://github.com/summer")
                                .email("contact@conversationvillage.com")
                        )
                )
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Local Development Server")
                )
                .addServersItem(new Server()
                        .url("http://13.125.244.156:8080")
                        .description("AWS EC2 Production Server")
                );
    }
}

