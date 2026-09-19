package com.fundoo.notes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "users")
@Getter 
@Setter
@Builder
@AllArgsConstructor 
@NoArgsConstructor 
public class User {
    
    @Id
    @Column (name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "first_name",nullable = false)  
    private String firstName;
    
    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "email",nullable = false,unique = true)
    private String email;
    
    @Column(name = "password",nullable = false)
    private String password;
}