package domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Enumerated(EnumType.STRING)
    private SenderType senderType;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;


    public static Message userMessage(Conversation conversation, String content) {
        return Message.builder()
                .conversation(conversation)
                .senderType(SenderType.USER)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Message aiMessage(Conversation conversation, String content) {
        return Message.builder()
                .conversation(conversation)
                .senderType(SenderType.AI)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
