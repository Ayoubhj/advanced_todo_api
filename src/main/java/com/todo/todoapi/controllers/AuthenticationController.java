package com.todo.todoapi.controllers;


import com.todo.todoapi.dto.AuthenticationResponse;
import com.todo.todoapi.dto.Login;
import com.todo.todoapi.dto.Register;
import com.todo.todoapi.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final UserService userService;

    /**
     * register function for create new user and token for access
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody Register request
    ){
       return ResponseEntity.ok(userService.register(request));
    }


    /**
     * login function for create token for user
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody Login request
    ){
        return ResponseEntity.ok(userService.login(request));
    }


    /**
     * refreshToken function for create new token from refresh token
     */
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        userService.refreshToken(request, response);
    }
}
