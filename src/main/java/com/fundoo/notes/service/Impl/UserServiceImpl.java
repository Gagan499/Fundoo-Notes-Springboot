package com.fundoo.notes.service.Impl;

import com.fundoo.notes.dto.RegistrationDTO;
import com.fundoo.notes.dto.UserResponseDTO;
import com.fundoo.notes.entity.User;
import com.fundoo.notes.execption.UserAlreadyExistsException;
import com.fundoo.notes.repository.UserRepository;
import com.fundoo.notes.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder ){
        this.userRepository = userRepository;
        this.passwordEncoder =passwordEncoder;
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
}