package com.fundoo.notes.service.Impl;

import com.fundoo.notes.dto.LoginDTO;
import com.fundoo.notes.dto.LoginResponseDTO;
import com.fundoo.notes.dto.RegistrationDTO;
import com.fundoo.notes.dto.UserResponseDTO;
import com.fundoo.notes.entity.User;
import com.fundoo.notes.execption.InvalidCredentialsException;
import com.fundoo.notes.execption.UserAlreadyExistsException;
import com.fundoo.notes.repository.UserRepository;
import com.fundoo.notes.service.UserService;
import com.fundoo.notes.util.JwtUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils){
        this.userRepository = userRepository;
        this.passwordEncoder =passwordEncoder;
        this.jwtUtils = jwtUtils;
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