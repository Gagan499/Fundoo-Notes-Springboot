package com.fundoo.notes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @NotBlank(message = "Email can't be empty")
    @Email(message = "Email is inValid")
    private String email;

    @NotBlank(message = "Password can't be empty")
    @NotNull(message = "Password can't be Null")
    private String password;
}
