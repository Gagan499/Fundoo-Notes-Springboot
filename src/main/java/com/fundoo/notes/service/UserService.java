package com.fundoo.notes.service;

import com.fundoo.notes.dto.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface  UserService {
    UserResponseDTO registerUser(RegistrationDTO registrationDTO);

    LoginResponseDTO login(LoginDTO loginDTO);

    void processForgotPassword(String email);

    void resetPassword(String token, ResetPasswordDTO resetPasswordDTO);
}
