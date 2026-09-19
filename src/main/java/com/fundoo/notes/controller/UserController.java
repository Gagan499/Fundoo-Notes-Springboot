package com.fundoo.notes.controller;

import com.fundoo.notes.dto.ApiResponse;
import com.fundoo.notes.dto.RegistrationDTO;
import com.fundoo.notes.dto.UserResponseDTO;
import com.fundoo.notes.service.Impl.UserServiceImpl;
import com.fundoo.notes.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(@Valid @RequestBody RegistrationDTO registrationDTO){
        UserResponseDTO result = userService.registerUser(registrationDTO);

        ApiResponse<UserResponseDTO> response = ApiResponse.success("User registered Successfully",result);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
