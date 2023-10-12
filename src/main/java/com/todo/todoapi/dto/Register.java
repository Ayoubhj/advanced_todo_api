package com.todo.todoapi.dto;

import com.todo.todoapi.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Register {


    @Size(max = 50,min = 3)
    @NotBlank
    @NotNull
    private String firstName;
    @Size(max = 50,min = 3)
    @NotBlank
    @NotNull
    private String lastName;
    @Email
    @NotBlank
    @NotNull
    private String email;
    @Size(min = 8 ,max = 120)
    @NotBlank()
    @NotNull
    private String password;
    @Size(min = 9 ,max = 50)
    private String phone;

    private String image;

    private Role role;

}
