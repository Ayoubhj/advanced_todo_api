package com.todo.todoapi.DTO;

import com.todo.todoapi.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Register {

    private String email;
    private String phone;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;

}
