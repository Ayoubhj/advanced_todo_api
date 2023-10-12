package com.todo.todoapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.todoapi.dto.*;
import com.todo.todoapi.entities.Token;
import com.todo.todoapi.entities.User;
import com.todo.todoapi.enums.TokenType;
import com.todo.todoapi.repositories.TokenRepository;
import com.todo.todoapi.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;


import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    /**
     * register function for create new user and token for access
     */
    public AuthenticationResponse login(Login request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var jwtToken = jwtService.generateToken(user);
        var jwtTokenRefresh = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder().user(user).refreshToken(jwtTokenRefresh).accessToken(jwtToken).build();

    }

    /**
     * login function for create token for user
     */
    public AuthenticationResponse register( Register request) {

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole())
                .image(request.getImage())
                .build();



        var userCheck =  userRepository.findByEmail(user.getEmail());

        if(userCheck.isPresent()){
            return null;
        }

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder().user(user).refreshToken(refreshToken).accessToken(jwtToken).build();

    }

    /**
     * saveUserToken is a function  for save token user into database
     */
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    /**
     * revokeAllUserTokens is a function  for revoke all user token
     */
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    /**
     * refreshToken function for create new token from refresh token
     */
    public AuthenticationResponse refreshToken(com.todo.todoapi.dto.Token token){
        String userEmail;
        if (token.getToken() == null ) {
            return null;
        }
        userEmail = jwtService.extractUsername(token.getToken());
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(token.getToken(), user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(token.getToken())
                        .user(user)
                        .build();
                return authResponse;
            }
        }
        return null;
    }

    public User getConnectedUser() {
       return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public User updateImage(ImageBase64 image,Long id) {
        User user = userRepository.findById(id).orElseThrow();
        if(Objects.isNull(user)){
            return null;
        }
        user.setImage(image.getImage());
        userRepository.save(user);
        return user;
    }

    public User updateUser(UserDto user) {
        User userExit = userRepository.findById(user.getUserId()).orElseThrow();
        if(Objects.isNull(userExit)){
            return null;
        }
        userExit.setFirstName(user.getFirstName());
        userExit.setLastName(user.getLastName());
        userExit.setEmail(user.getEmail());
        userExit.setPhone(user.getPhone());
        userRepository.save(userExit);
        return userExit;
    }

    public User updatePassword(PasswordChange change, Long id) {
        User userExit = userRepository.findById(id).orElseThrow();
        if(Objects.isNull(userExit)){
            return null;
        }
        if(!passwordEncoder.matches(change.getOldPassword(),userExit.getPassword())){
            return null;
        }
        userExit.setPassword(passwordEncoder.encode(change.getNewPassword()));
        userRepository.save(userExit);
        return userExit;
    }


}
