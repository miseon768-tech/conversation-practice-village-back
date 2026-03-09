package com.example.conversationpracticevillageback.domain.service;

import com.example.conversationpracticevillageback.domain.entity.Conversation;
import com.example.conversationpracticevillageback.domain.entity.Member;
import com.example.conversationpracticevillageback.domain.entity.Persona;
import com.example.conversationpracticevillageback.domain.repository.ConversationRepository;
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
@DisplayName("ConversationService 테스트")
class ConversationServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private ConversationRepository conversationRepository;

    @InjectMocks
    private ConversationService conversationService;

    private Member testMember;
    private Persona testPersona;
    private Conversation testConversation;

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
    }

    @Test
    @DisplayName("대화방 조회 또는 생성 - 기존 대화방 조회")
    void testGetOrCreateConversation_ExistingConversation() {
        // Given
        List<Conversation> conversations = Arrays.asList(testConversation);
        when(personaRepository.findById(1L)).thenReturn(Optional.of(testPersona));
        when(conversationRepository.findByPersona_IdOrderByIdDesc(1L)).thenReturn(conversations);

        // When
        Long result = conversationService.getOrCreateConversation(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result);
        verify(personaRepository, times(1)).findById(1L);
        verify(conversationRepository, times(1)).findByPersona_IdOrderByIdDesc(1L);
        verify(conversationRepository, never()).save(any(Conversation.class));
    }

    @Test
    @DisplayName("대화방 조회 또는 생성 - 새로운 대화방 생성")
    void testGetOrCreateConversation_CreateNewConversation() {
        // Given
        Conversation newConversation = Conversation.builder()
                .id(2L)
                .persona(testPersona)
                .createdAt(LocalDateTime.now())
                .build();

        when(personaRepository.findById(1L)).thenReturn(Optional.of(testPersona));
        when(conversationRepository.findByPersona_IdOrderByIdDesc(1L)).thenReturn(Arrays.asList());
        when(conversationRepository.save(any(Conversation.class))).thenReturn(newConversation);

        // When
        Long result = conversationService.getOrCreateConversation(1L);

        // Then
        assertNotNull(result);
        assertEquals(2L, result);
        verify(personaRepository, times(1)).findById(1L);
        verify(conversationRepository, times(1)).findByPersona_IdOrderByIdDesc(1L);
        verify(conversationRepository, times(1)).save(any(Conversation.class));
    }

    @Test
    @DisplayName("대화방 조회 또는 생성 - 페르소나 없음")
    void testGetOrCreateConversation_PersonaNotFound() {
        // Given
        when(personaRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            conversationService.getOrCreateConversation(999L);
        });
        verify(personaRepository, times(1)).findById(999L);
        verify(conversationRepository, never()).findByPersona_IdOrderByIdDesc(any());
    }

    @Test
    @DisplayName("페르소나별 대화방 목록 조회 - 성공")
    void testGetConversationsByPersona_Success() {
        // Given
        Conversation conversation2 = Conversation.builder()
                .id(2L)
                .persona(testPersona)
                .createdAt(LocalDateTime.now())
                .build();

        List<Conversation> conversations = Arrays.asList(conversation2, testConversation);
        when(conversationRepository.findByPersona_IdOrderByIdDesc(1L)).thenReturn(conversations);

        // When
        List<Conversation> result = conversationService.getConversationsByPersona(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getId());
        assertEquals(1L, result.get(1).getId());
        verify(conversationRepository, times(1)).findByPersona_IdOrderByIdDesc(1L);
    }

    @Test
    @DisplayName("페르소나별 대화방 목록 조회 - 빈 리스트")
    void testGetConversationsByPersona_Empty() {
        // Given
        when(conversationRepository.findByPersona_IdOrderByIdDesc(999L)).thenReturn(Arrays.asList());

        // When
        List<Conversation> result = conversationService.getConversationsByPersona(999L);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(conversationRepository, times(1)).findByPersona_IdOrderByIdDesc(999L);
    }

    @Test
    @DisplayName("대화방 엔티티 속성 검증")
    void testConversationAttributes() {
        // When
        testConversation.setPersona(testPersona);

        // Then
        assertNotNull(testConversation.getId());
        assertNotNull(testConversation.getPersona());
        assertEquals("영희", testConversation.getPersona().getName());
        assertNotNull(testConversation.getCreatedAt());
    }

    @Test
    @DisplayName("대화방 빌더 패턴 테스트")
    void testConversationBuilder() {
        // When
        Conversation conversation = Conversation.builder()
                .id(10L)
                .persona(testPersona)
                .createdAt(LocalDateTime.now())
                .build();

        // Then
        assertEquals(10L, conversation.getId());
        assertEquals("영희", conversation.getPersona().getName());
        assertNotNull(conversation.getCreatedAt());
    }
}

