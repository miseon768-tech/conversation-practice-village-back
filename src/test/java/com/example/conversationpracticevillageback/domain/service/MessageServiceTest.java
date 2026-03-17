package com.example.conversationpracticevillageback.domain.service;

import com.example.conversationpracticevillageback.domain.AI.AiService;
import com.example.conversationpracticevillageback.domain.response.MessageDto;
import com.example.conversationpracticevillageback.domain.response.ChatResponse;
import com.example.conversationpracticevillageback.domain.entity.Conversation;
import com.example.conversationpracticevillageback.domain.entity.Member;
import com.example.conversationpracticevillageback.domain.entity.Message;
import com.example.conversationpracticevillageback.domain.entity.Persona;
import com.example.conversationpracticevillageback.domain.entity.SenderType;
import com.example.conversationpracticevillageback.domain.repository.ConversationRepository;
import com.example.conversationpracticevillageback.domain.repository.MessageRepository;
import com.example.conversationpracticevillageback.domain.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MessageService 테스트")
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private AiService aiService;

    @InjectMocks
    private MessageService messageService;

    private Member testMember;
    private Persona testPersona;
    private Conversation testConversation;
    private Message userMessage;
    private Message aiMessage;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L)
                .email("test@example.com")
                .nickname("테스트유저")
                .password("password123")
                .createdAt(LocalDateTime.now())
                .build();

        testPersona = Persona.builder()
                .id(1L)
                .member(testMember)
                .npcId("npc_001")
                .name("영희")
                .age(25)
                .job("카페 알바생")
                .mbti("ENFP")
                .relationshipType("친구")
                .personalityKeywords("활발함, 친절함")
                .speechStyle("존댓말")
                .intimacyScore(0)
                .trustScore(0)
                .createdAt(LocalDateTime.now())
                .build();

        testConversation = Conversation.builder()
                .id(1L)
                .persona(testPersona)
                .createdAt(LocalDateTime.now())
                .build();

        userMessage = Message.builder()
                .id(1L)
                .conversation(testConversation)
                .senderType(SenderType.USER)
                .content("안녕하세요!")
                .createdAt(LocalDateTime.now())
                .build();

        aiMessage = Message.builder()
                .id(2L)
                .conversation(testConversation)
                .senderType(SenderType.AI)
                .content("안녕하세요! 뵙게 되어 반가워요.")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("메시지 전송 및 AI 응답 처리 - 성공")
    void testSendMessage_Success() {
        // Given
        String userContent = "안녕하세요!";
        String aiReply = "안녕하세요! 뵙게 되어 반가워요.";

        when(conversationRepository.findById(1L)).thenReturn(Optional.of(testConversation));
        when(messageRepository.save(any(Message.class)))
                .thenReturn(userMessage)
                .thenReturn(aiMessage);
        when(aiService.generateReply(testPersona, userContent)).thenReturn(aiReply);

        // When
        ChatResponse result = messageService.sendMessage(1L, userContent);

        // Then
        assertNotNull(result);
        assertEquals(aiReply, result.getReply());
        verify(conversationRepository, times(1)).findById(1L);
        verify(messageRepository, times(2)).save(any(Message.class));
        verify(aiService, times(1)).generateReply(testPersona, userContent);
    }

    @Test
    @DisplayName("메시지 전송 - 실패 (존재하지 않는 대화방)")
    void testSendMessage_ConversationNotFound() {
        // Given
        when(conversationRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            messageService.sendMessage(999L, "안녕하세요!");
        });
        verify(conversationRepository, times(1)).findById(999L);
        verify(messageRepository, never()).save(any(Message.class));
        verify(aiService, never()).generateReply(any(), any());
    }

    @Test
    @DisplayName("대화 내역 조회 - 성공")
    void testGetMessagesByConversation_Success() {
        // Given
        List<Message> messages = Arrays.asList(userMessage, aiMessage);
        when(messageRepository.findByConversation_Persona_IdOrderByCreatedAtAsc(1L))
                .thenReturn(messages);

        // When
        List<MessageDto> result = messageService.getMessagesByConversation(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(messageRepository, times(1)).findByConversation_Persona_IdOrderByCreatedAtAsc(1L);
    }

    @Test
    @DisplayName("대화 내역 조회 - 빈 리스트")
    void testGetMessagesByConversation_Empty() {
        // Given
        when(messageRepository.findByConversation_Persona_IdOrderByCreatedAtAsc(999L))
                .thenReturn(Arrays.asList());

        // When
        List<MessageDto> result = messageService.getMessagesByConversation(999L);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(messageRepository, times(1)).findByConversation_Persona_IdOrderByCreatedAtAsc(999L);
    }

    @Test
    @DisplayName("메시지 팩토리 메서드 - 사용자 메시지")
    void testUserMessageFactory() {
        // When
        Message result = Message.userMessage(testConversation, "테스트 메시지");

        // Then
        assertNotNull(result);
        assertEquals(SenderType.USER, result.getSenderType());
        assertEquals("테스트 메시지", result.getContent());
        assertEquals(testConversation, result.getConversation());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    @DisplayName("메시지 팩토리 메서드 - AI 메시지")
    void testAiMessageFactory() {
        // When
        Message result = Message.aiMessage(testConversation, "AI 응답");

        // Then
        assertNotNull(result);
        assertEquals(SenderType.AI, result.getSenderType());
        assertEquals("AI 응답", result.getContent());
        assertEquals(testConversation, result.getConversation());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    @DisplayName("메시지 타입 열거형 검증")
    void testSenderTypeEnum() {
        // When & Then
        assertEquals(SenderType.USER, SenderType.valueOf("USER"));
        assertEquals(SenderType.AI, SenderType.valueOf("AI"));
        assertEquals(2, SenderType.values().length);
    }
}


