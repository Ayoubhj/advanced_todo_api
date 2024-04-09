package com.todo.todoapi.controllers;


import com.todo.todoapi.dto.*;
import com.todo.todoapi.entities.User;
import com.todo.todoapi.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {


    private final UserService userService;

    /**
     * register function for create new user and token for access
     */
    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResponseEntity<AuthenticationResponse> register(
             @RequestBody @Valid Register request
    ){
        AuthenticationResponse response = userService.register(request);
        log.info("user is register {}",response.getUser().getUsername());
       return ResponseEntity.ok(response);
    }


    /**
     * login function for create token for user
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
             @RequestBody @Valid  Login request
    ){
        AuthenticationResponse response = userService.login(request);
        log.info("user is logged in {}",response.getUser().getUsername());
        return ResponseEntity.ok(response);
    }


    /**
     * refreshToken function for create new token from refresh token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody Token token)  {
        return ResponseEntity.ok(userService.refreshToken(token));
    }



}
