package com.example.conversationpracticevillageback.domain.controller;

import com.example.conversationpracticevillageback.domain.response.MessageDto;
import com.example.conversationpracticevillageback.domain.request.ChatRequest;
import com.example.conversationpracticevillageback.domain.response.ChatResponse;
import com.example.conversationpracticevillageback.domain.entity.SenderType;
import com.example.conversationpracticevillageback.domain.service.MessageService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
@DisplayName("MessageController 테스트")
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("메시지 전송 - 성공")
    void testSendMessage_Success() throws Exception {
        ChatRequest request = new ChatRequest("안녕하세요!");
        ChatResponse response = new ChatResponse("안녕하세요! 반가워요.");

        when(messageService.sendMessage(1L, "안녕하세요!")).thenReturn(response);

        mockMvc.perform(post("/api/messages/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(messageService, times(1)).sendMessage(1L, "안녕하세요!");
    }

    @Test
    @DisplayName("대화 내역 조회 - 성공")
    void testGetMessages_Success() throws Exception {
        MessageDto msg1 = MessageDto.builder()
                .id(1L)
                .content("안녕하세요!")
                .senderType(SenderType.USER)
                .createdAt(LocalDateTime.now())
                .build();

        when(messageService.getMessagesByConversation(1L)).thenReturn(Arrays.asList(msg1));

        mockMvc.perform(get("/api/messages/1"))
                .andExpect(status().isOk());

        verify(messageService, times(1)).getMessagesByConversation(1L);
    }

    @Test
    @DisplayName("대화 내역 조회 - 빈 리스트")
    void testGetMessages_Empty() throws Exception {
        when(messageService.getMessagesByConversation(999L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/messages/999"))
                .andExpect(status().isOk());

        verify(messageService, times(1)).getMessagesByConversation(999L);
    }
}






