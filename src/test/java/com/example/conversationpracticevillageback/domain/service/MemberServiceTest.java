package com.example.conversationpracticevillageback.domain.service;

import com.example.conversationpracticevillageback.domain.entity.Member;
import com.example.conversationpracticevillageback.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService 테스트")
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L)
                .email("test@example.com")
                .nickname("테스트유저")
                .password("password123")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("회원 가입 - 성공")
    void testCreateMember_Success() {
        // Given
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        // When
        Member result = memberService.create(testMember);

        // Then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("테스트유저", result.getNickname());
        verify(memberRepository, times(1)).save(testMember);
    }

    @Test
    @DisplayName("로그인 - 성공")
    void testLogin_Success() {
        // Given
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testMember));

        // When
        Member result = memberService.login("test@example.com", "password123");

        // Then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(memberRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("로그인 - 실패 (존재하지 않는 이메일)")
    void testLogin_UserNotFound() {
        // Given
        when(memberRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            memberService.login("notfound@example.com", "password123");
        });
        verify(memberRepository, times(1)).findByEmail("notfound@example.com");
    }

    @Test
    @DisplayName("로그인 - 실패 (잘못된 비밀번호)")
    void testLogin_WrongPassword() {
        // Given
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testMember));

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            memberService.login("test@example.com", "wrongpassword");
        });
        verify(memberRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("회원 정보 조회 - 성공")
    void testGetMember_Success() {
        // Given
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));

        // When
        Member result = memberService.get(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("회원 정보 조회 - 실패 (존재하지 않는 회원)")
    void testGetMember_NotFound() {
        // Given
        when(memberRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            memberService.get(999L);
        });
        verify(memberRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("모든 회원 조회 - 성공")
    void testGetAllMembers_Success() {
        // Given
        Member member2 = Member.builder()
                .id(2L)
                .email("test2@example.com")
                .nickname("테스트유저2")
                .password("password456")
                .build();

        List<Member> memberList = Arrays.asList(testMember, member2);
        when(memberRepository.findAll()).thenReturn(memberList);

        // When
        List<Member> result = memberService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
        assertEquals("test2@example.com", result.get(1).getEmail());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("회원 삭제 - 성공")
    void testDeleteMember_Success() {
        // Given
        doNothing().when(memberRepository).deleteById(1L);

        // When
        memberService.delete(1L);

        // Then
        verify(memberRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("로그인 - null 비밀번호 처리")
    void testLogin_NullPassword() {
        // Given
        Member memberWithoutPassword = Member.builder()
                .id(1L)
                .email("test@example.com")
                .nickname("테스트유저")
                .password(null)
                .build();

        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(memberWithoutPassword));

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            memberService.login("test@example.com", "anypassword");
        });
    }
}

