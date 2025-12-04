package com.project.mymemory.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
