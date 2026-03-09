package com.example.conversationpracticevillageback.domain.controller;

import com.example.conversationpracticevillageback.domain.entity.Conversation;
import com.example.conversationpracticevillageback.domain.entity.Persona;
import com.example.conversationpracticevillageback.domain.service.ConversationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConversationController.class)
@DisplayName("ConversationController 테스트")
class ConversationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConversationService conversationService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("대화방 조회 또는 생성 - 새로운 대화방 생성")
    void testGetOrCreateConversation_CreateNew() throws Exception {
        when(conversationService.getOrCreateConversation(1L)).thenReturn(1L);

        mockMvc.perform(post("/api/conversations/persona/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(conversationService, times(1)).getOrCreateConversation(1L);
    }

    @Test
    @DisplayName("대화방 조회 또는 생성 - 기존 대화방 반환")
    void testGetOrCreateConversation_ReturnExisting() throws Exception {
        when(conversationService.getOrCreateConversation(1L)).thenReturn(5L);

        mockMvc.perform(post("/api/conversations/persona/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(conversationService, times(1)).getOrCreateConversation(1L);
    }

    @Test
    @DisplayName("페르소나별 대화방 목록 조회 - 성공")
    void testGetConversationList_Success() throws Exception {
        Persona persona = Persona.builder()
                .id(1L)
                .name("영희")
                .build();

        Conversation conv1 = Conversation.builder()
                .id(1L)
                .persona(persona)
                .createdAt(LocalDateTime.now())
                .build();

        when(conversationService.getConversationsByPersona(1L)).thenReturn(Arrays.asList(conv1));

        mockMvc.perform(get("/api/conversations/persona/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(conversationService, times(1)).getConversationsByPersona(1L);
    }

    @Test
    @DisplayName("페르소나별 대화방 목록 조회 - 빈 리스트")
    void testGetConversationList_Empty() throws Exception {
        when(conversationService.getConversationsByPersona(999L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/conversations/persona/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(conversationService, times(1)).getConversationsByPersona(999L);
    }
}


