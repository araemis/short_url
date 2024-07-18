package com.example.demo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordDto {
    @NotBlank(message = "Password is mandatory")
    private String password;

}
