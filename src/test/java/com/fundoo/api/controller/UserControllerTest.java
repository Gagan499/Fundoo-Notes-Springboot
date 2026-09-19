package com.fundoo.notes.controller;

import com.fundoo.notes.FundooApiApplication;
import com.fundoo.notes.dto.RegistrationDTO;
import com.fundoo.notes.dto.UserResponseDTO;
import com.fundoo.notes.execption.UserAlreadyExistsException;
import com.fundoo.notes.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = FundooApiApplication.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private RegistrationDTO dto;


    @BeforeEach
    void setup() {

        dto = new RegistrationDTO();

        dto.setFirstName("Gagan");
        dto.setLastName("Singh");
        dto.setEmail("gagan@gmail.com");
        dto.setPassword("Password@123");
    }


    @Test
    void registration_ShouldReturn201_whenRegistrationSuccess() throws Exception {

        UserResponseDTO response = new UserResponseDTO();

        when(userService.registerUser(any(RegistrationDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }


    @Test
    void registerUser_duplicateEmail_returns409() throws Exception {

        when(userService.registerUser(any(RegistrationDTO.class)))
                .thenThrow(
                        new UserAlreadyExistsException("Email already exists")
                );

        mockMvc.perform(
                        post("/api/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(
                        jsonPath("$.message")
                                .value("Email already exists")
                );
    }


    @Test
    void registerUser_invalidFirstName_returns400() throws Exception {

        dto.setFirstName("");

        mockMvc.perform(
                        post("/api/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors").exists());

        verify(
                userService,
                never()
        ).registerUser(any(RegistrationDTO.class));
    }
}