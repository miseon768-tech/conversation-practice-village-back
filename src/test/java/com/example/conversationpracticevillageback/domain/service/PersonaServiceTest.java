package com.example.conversationpracticevillageback.domain.service;

import com.example.conversationpracticevillageback.domain.entity.Member;
import com.example.conversationpracticevillageback.domain.entity.Persona;
import com.example.conversationpracticevillageback.domain.repository.MemberRepository;
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
@DisplayName("PersonaService 테스트")
class PersonaServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private PersonaService personaService;

    private Member testMember;
    private Persona testPersona;

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
    }

    @Test
    @DisplayName("페르소나 생성 - 성공")
    void testCreatePersona_Success() {
        // Given
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));
        when(personaRepository.existsByNpcId("npc_001")).thenReturn(false);
        when(personaRepository.save(any(Persona.class))).thenReturn(testPersona);

        // When
        Persona result = personaService.create(1L, "npc_001", testPersona);

        // Then
        assertNotNull(result);
        assertEquals("영희", result.getName());
        assertEquals("npc_001", result.getNpcId());
        assertEquals(testMember, result.getMember());
        verify(memberRepository, times(1)).findById(1L);
        verify(personaRepository, times(1)).existsByNpcId("npc_001");
        verify(personaRepository, times(1)).save(any(Persona.class));
    }

    @Test
    @DisplayName("페르소나 생성 - 실패 (이미 인격이 부여된 NPC)")
    void testCreatePersona_DuplicateNpc() {
        // Given
        when(personaRepository.existsByNpcId("npc_001")).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            personaService.create(1L, "npc_001", testPersona);
        });
        verify(personaRepository, times(1)).existsByNpcId("npc_001");
        verify(memberRepository, never()).findById(any());
    }

    @Test
    @DisplayName("페르소나 생성 - 실패 (존재하지 않는 회원)")
    void testCreatePersona_MemberNotFound() {
        // Given
        when(personaRepository.existsByNpcId("npc_001")).thenReturn(false);
        when(memberRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            personaService.create(999L, "npc_001", testPersona);
        });
        verify(personaRepository, times(1)).existsByNpcId("npc_001");
        verify(memberRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("페르소나 조회 - 성공")
    void testGetPersona_Success() {
        // Given
        when(personaRepository.findById(1L)).thenReturn(Optional.of(testPersona));

        // When
        Persona result = personaService.get(1L);

        // Then
        assertNotNull(result);
        assertEquals("영희", result.getName());
        assertEquals(1L, result.getId());
        verify(personaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("페르소나 조회 - 실패 (존재하지 않는 페르소나)")
    void testGetPersona_NotFound() {
        // Given
        when(personaRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            personaService.get(999L);
        });
        verify(personaRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("회원별 페르소나 조회 - 성공")
    void testGetPersonasByMember_Success() {
        // Given
        Persona persona2 = Persona.builder()
                .id(2L)
                .member(testMember)
                .npcId("npc_002")
                .name("철수")
                .age(30)
                .job("개발자")
                .build();

        List<Persona> personaList = Arrays.asList(testPersona, persona2);
        when(personaRepository.findByMemberId(1L)).thenReturn(personaList);

        // When
        List<Persona> result = personaService.getByMember(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("영희", result.get(0).getName());
        assertEquals("철수", result.get(1).getName());
        verify(personaRepository, times(1)).findByMemberId(1L);
    }

    @Test
    @DisplayName("회원별 페르소나 조회 - 빈 리스트")
    void testGetPersonasByMember_Empty() {
        // Given
        when(personaRepository.findByMemberId(999L)).thenReturn(Arrays.asList());

        // When
        List<Persona> result = personaService.getByMember(999L);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(personaRepository, times(1)).findByMemberId(999L);
    }

    @Test
    @DisplayName("페르소나 삭제 - 성공")
    void testDeletePersona_Success() {
        // Given
        doNothing().when(personaRepository).deleteById(1L);

        // When
        personaService.delete(1L);

        // Then
        verify(personaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("페르소나 속성 검증")
    void testPersonaAttributes() {
        // When
        testPersona.setIntimacyScore(50);
        testPersona.setTrustScore(75);

        // Then
        assertEquals(50, testPersona.getIntimacyScore());
        assertEquals(75, testPersona.getTrustScore());
        assertEquals("ENFP", testPersona.getMbti());
        assertEquals("친구", testPersona.getRelationshipType());
    }
}

