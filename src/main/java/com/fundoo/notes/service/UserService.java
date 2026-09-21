package com.fundoo.notes.service;

import com.fundoo.notes.dto.LoginDTO;
import com.fundoo.notes.dto.LoginResponseDTO;
import com.fundoo.notes.dto.UserResponseDTO;
import com.fundoo.notes.dto.RegistrationDTO;

public interface  UserService {
    UserResponseDTO registerUser(RegistrationDTO registrationDTO);

    LoginResponseDTO login(LoginDTO loginDTO);
}
