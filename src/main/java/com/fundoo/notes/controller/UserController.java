package com.fundoo.notes.controller;

import com.fundoo.notes.dto.*;
import com.fundoo.notes.service.EmailService;
import com.fundoo.notes.service.Impl.UserServiceImpl;
import com.fundoo.notes.service.UserService;
import com.fundoo.notes.util.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(@Valid @RequestBody RegistrationDTO registrationDTO) {
        UserResponseDTO result = userService.registerUser(registrationDTO);

        ApiResponse<UserResponseDTO> response = ApiResponse.success("User registered Successfully", result);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginDTO loginDTO){
        LoginResponseDTO result = userService.login(loginDTO);

        ApiResponse<LoginResponseDTO> response = ApiResponse.success("User Login Successfully",result);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<ForgotPasswordDTO>> forgotpassword (@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO){
        userService.processForgotPassword(forgotPasswordDTO.getEmail());
        ApiResponse<ForgotPasswordDTO> response = ApiResponse.success("If an accound exists with these email, reset password link has been sent !",null);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/reset-password")
    public  ResponseEntity<ApiResponse<ResetPasswordDTO>> resetpassword(@RequestParam String token ,@Valid @RequestBody ResetPasswordDTO resetPasswordDTO){
        userService.resetPassword(token,resetPasswordDTO);
        ApiResponse<ResetPasswordDTO> response = ApiResponse.success("Password reset successfully",null);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
