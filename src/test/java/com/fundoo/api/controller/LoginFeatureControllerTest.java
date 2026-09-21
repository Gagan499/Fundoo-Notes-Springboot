package com.fundoo.api.controller;

import com.fundoo.notes.FundooApiApplication;
import com.fundoo.notes.dto.LoginDTO;
import com.fundoo.notes.dto.LoginResponseDTO;
import com.fundoo.notes.execption.InvalidCredentialsException;
import com.fundoo.notes.execption.UserAlreadyExistsException;
import com.fundoo.notes.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(classes = FundooApiApplication.class)
@AutoConfigureMockMvc
class LoginFeatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private LoginDTO loginDTO;

    @BeforeEach
    void setup() {
        loginDTO = new LoginDTO();
        loginDTO.setEmail("syangagandeep2305@gmail.com");
        loginDTO.setPassword("mju7&UJM");
    }

    // 1. SUCCESSFUL LOGIN
    @Test
    void login_should_return200() throws Exception {

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        when(userService.login(any(LoginDTO.class)))
                .thenReturn(loginResponseDTO);

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // 2. INVALID PASSWORD
    @Test
    void login_should_return401_when_password_is_invalid() throws Exception {

        loginDTO.setEmail("john@gmail.com");
        loginDTO.setPassword("mju");
        when(userService.login(any(LoginDTO.class)))
                .thenThrow(new InvalidCredentialsException("Invalid or wrong password"));

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO))
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    // 3. USER NOT FOUND
    @Test
    void login_should_return404_when_user_not_found() throws Exception {

        loginDTO.setEmail("john@gmail.com");
        loginDTO.setPassword("mk");
        when(userService.login(any(LoginDTO.class)))
                .thenThrow(new UsernameNotFoundException("User not found"));

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO))
                )
                .andExpect(status().isNotFound());
    }

    // 4. EMPTY EMAIL
    @Test
    void login_should_return400_when_email_is_empty() throws Exception {

        loginDTO.setEmail("");

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    // 5. EMPTY PASSWORD
    @Test
    void login_should_return400_when_password_is_empty() throws Exception {

        loginDTO.setPassword("");

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    // 6. BOTH EMAIL AND PASSWORD EMPTY
    @Test
    void login_should_return400_when_email_and_password_are_empty() throws Exception {

        loginDTO.setEmail("");
        loginDTO.setPassword("");

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    // 7. INVALID EMAIL FORMAT
    @Test
    void login_should_return400_when_email_is_invalid() throws Exception {

        loginDTO.setEmail("invalid-email");

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    // 8. MISSING EMAIL
    @Test
    void login_should_return400_when_email_is_missing() throws Exception {

        loginDTO.setEmail(null);

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO))
                )
                .andExpect(status().isBadRequest());
    }

    // 9. MISSING PASSWORD
    @Test
    void login_should_return400_when_password_is_missing() throws Exception {

        loginDTO.setPassword(null);

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO))
                )
                .andExpect(status().isBadRequest());
    }
}
