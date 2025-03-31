package com.example.blog.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String avatar;

    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    private LocalDate dob;
    
    @Column(nullable = true)
    private String phone_number;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public String getAvatarBase64() {
        return (avatar != null && !avatar.isEmpty()) ? "data:image/png;base64," + avatar : "/images/default-avatar.jpg";
    }
    
}
