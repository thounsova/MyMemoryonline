package com.project.mymemory.entitys;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User fields
    private String fullname;
    private String username;
    private String email;
    private String password;
//    private String role;
}
