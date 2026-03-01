package domain.dto;

import domain.entity.Message;
import domain.entity.SenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private Long id;
    private String content;
    private SenderType senderType;
    private LocalDateTime createdAt;

    public static MessageDto from(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .senderType(message.getSenderType())
                .createdAt(message.getCreatedAt())
                .build();
    }
}