package com.fundoo.notes.service.Impl;

import com.fundoo.notes.dto.*;
import com.fundoo.notes.entity.User;
import com.fundoo.notes.execption.InvalidCredentialsException;
import com.fundoo.notes.execption.UserAlreadyExistsException;
import com.fundoo.notes.repository.UserRepository;
import com.fundoo.notes.service.EmailService;
import com.fundoo.notes.service.UserService;
import com.fundoo.notes.util.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;
    @Value("${app.frontend.reset-password-url:http://localhost:8080/reset-password}")
    private String resetPasswordUrl;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils,EmailService emailService){
        this.userRepository = userRepository;
        this.passwordEncoder =passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.emailService=emailService;
    }

    @Override
    public UserResponseDTO registerUser(RegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email : "+registrationDTO.getEmail());
        }
        User user = mapUserToEntity(registrationDTO);
        User savedUser = userRepository.save(user);

        return MapUserToResponse(savedUser);
    }

    @Override
    public LoginResponseDTO login(LoginDTO dto){
        User user  = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(()-> new InvalidCredentialsException("Invalid Credentional"));
        if(!passwordEncoder.matches(dto.getPassword(),user.getPassword())){
            throw new InvalidCredentialsException("Invalid or Wrong password");
        }
        String token = jwtUtils.generateToken(dto.getEmail());
        return MapUserDataLoginResponseDTO(user,token);
    }

    @Override
    public void processForgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("user not found with email"));
        String resetToken = jwtUtils.generateresettoken(email,user.getUserId());
        String resetLink = resetPasswordUrl + "?token=" + resetToken;
        emailService.sendPasswordResetEmail(user.getEmail(),resetLink);
    }

    @Override
    public void resetPassword(String token, ResetPasswordDTO resetPasswordDTO) {
        Long userId = jwtUtils.extractUserIdFromToken(token);
        if(userId==null){
            throw new UsernameNotFoundException("Invalid token: user Id not found");
        }
        User user = userRepository.findById(userId).orElseThrow(()->new UsernameNotFoundException("user not found with id : "+userId));
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    // Mapping Register Data To User
    private User mapUserToEntity(RegistrationDTO dto){
        User u = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        return u;
    }

    // Mapping User Data To Response
    private  UserResponseDTO MapUserToResponse(User user){
        return new UserResponseDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    // Mapping User Data To LoginResponseDTO
    private LoginResponseDTO MapUserDataLoginResponseDTO(User user,String token){
        return new LoginResponseDTO(
                token,
                user.getUserId(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}