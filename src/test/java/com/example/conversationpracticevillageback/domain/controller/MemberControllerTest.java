package com.example.conversationpracticevillageback.domain.controller;

import com.example.conversationpracticevillageback.domain.request.LoginRequest;
import com.example.conversationpracticevillageback.domain.entity.Member;
import com.example.conversationpracticevillageback.domain.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
@DisplayName("MemberController 테스트")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testCreateMember_Success() throws Exception {
        when(memberService.create(any(Member.class))).thenReturn(testMember);

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMember)))
                .andExpect(status().isOk());

        verify(memberService, times(1)).create(any(Member.class));
    }

    @Test
    @DisplayName("로그인 - 성공")
    void testLogin_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        when(memberService.login("test@example.com", "password123")).thenReturn(testMember);

        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        verify(memberService, times(1)).login("test@example.com", "password123");
    }

    @Test
    @DisplayName("로그인 - 실패 (Unauthorized)")
    void testLogin_Unauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongpassword");

        when(memberService.login(anyString(), anyString()))
                .thenThrow(new ResponseStatusException(
                        org.springframework.http.HttpStatus.UNAUTHORIZED,
                        "이메일 또는 비밀번호가 일치하지 않습니다."));

        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());

        verify(memberService, times(1)).login(anyString(), anyString());
    }

    @Test
    @DisplayName("회원 정보 조회 - 성공")
    void testGetMember_Success() throws Exception {
        when(memberService.get(1L)).thenReturn(testMember);

        mockMvc.perform(get("/api/members/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(memberService, times(1)).get(1L);
    }

    @Test
    @DisplayName("회원 삭제 - 성공")
    void testDeleteMember_Success() throws Exception {
        doNothing().when(memberService).delete(1L);

        mockMvc.perform(delete("/api/members/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(memberService, times(1)).delete(1L);
    }
}


