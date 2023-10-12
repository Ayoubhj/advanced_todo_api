package com.todo.todoapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    @NotBlank
    @Email
    private String email;
    @NotBlank()
    @Size(min = 8 ,max = 120)
    private String password;
}
