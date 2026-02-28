package domain.dto;

import domain.entity.Message;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageDto {

    private Long id;
    private String sender;   // user or ai
    private String content;
    private LocalDateTime createdAt;

    public static MessageDto from(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .sender(message.getSender())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}