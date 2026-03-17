package com.example.conversationpracticevillageback.domain.controller;

import com.example.conversationpracticevillageback.domain.request.PersonaRequest;
import com.example.conversationpracticevillageback.domain.entity.Persona;
import com.example.conversationpracticevillageback.domain.service.PersonaService;
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

@WebMvcTest(PersonaController.class)
@DisplayName("PersonaController 테스트")
class PersonaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonaService personaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Persona testPersona;

    @BeforeEach
    void setUp() {
        testPersona = Persona.builder()
                .id(1L)
                .npcId("npc_001")
                .name("영희")
                .age(25)
                .job("카페 알바생")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("페르소나 생성 - 성공")
    void testCreatePersona_Success() throws Exception {
        PersonaRequest request = new PersonaRequest();
        request.setMemberId(1L);
        request.setNpcId("npc_001");
        request.setName("영희");
        request.setAge(25);

        when(personaService.create(any(Long.class), any(String.class), any(Persona.class)))
                .thenReturn(testPersona);

        mockMvc.perform(post("/api/personas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(personaService, times(1)).create(any(Long.class), any(String.class), any(Persona.class));
    }

    @Test
    @DisplayName("페르소나 조회 - 성공")
    void testGetPersona_Success() throws Exception {
        when(personaService.get(1L)).thenReturn(testPersona);

        mockMvc.perform(get("/api/personas/1"))
                .andExpect(status().isOk());

        verify(personaService, times(1)).get(1L);
    }

    @Test
    @DisplayName("회원별 페르소나 조회 - 성공")
    void testGetPersonasByMember_Success() throws Exception {
        when(personaService.getByMember(1L)).thenReturn(Arrays.asList(testPersona));

        mockMvc.perform(get("/api/personas/member/1"))
                .andExpect(status().isOk());

        verify(personaService, times(1)).getByMember(1L);
    }
}




