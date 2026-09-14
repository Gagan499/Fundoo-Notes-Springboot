package com.fundoo.notes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
public class User {
    
    @Id
    @Column (name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(name = "first_name",nullable = false)  
    private String firstName;
    
    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "email",nullable = false,unique = true)
    @Pattern (
        regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
        message = "invalid email format"
    )
    private String email;
    
    @Column(name = "password",nullable = false)
    @Pattern (
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@&^%#])[a-zA-Z0-9@&^%#]{8,}$",
        message = "invalid password format"
    )
    private String password;
}