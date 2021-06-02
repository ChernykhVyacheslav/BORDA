package com.softserve.borda.controllers;

import com.softserve.borda.authorization.AuthRequest;
import com.softserve.borda.authorization.AuthResponse;
import com.softserve.borda.authorization.RegistrationRequest;
import com.softserve.borda.config.jwt.JwtConvertor;
import com.softserve.borda.config.jwt.JwtProvider;
import com.softserve.borda.entities.User;
import com.softserve.borda.exceptions.CustomAuthenticationFailedException;
import com.softserve.borda.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Log
@CrossOrigin
@Validated
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtConvertor jwtConvertor;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegistrationRequest registrationRequest)
            throws CustomAuthenticationFailedException {
        try {
            User user = new User();
            user.setUsername(registrationRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setEmail(registrationRequest.getEmail());
            user.setFirstName(registrationRequest.getFirstName());
            user.setLastName(registrationRequest.getLastName());
            userService.create(user);
            jwtConvertor.saveUser(user);
        } catch (Exception e) {
            log.warning(e.getMessage());
            throw new CustomAuthenticationFailedException("Registration failed");
        }
        AuthRequest authRequest = modelMapper.map(registrationRequest, AuthRequest.class);
        return auth(authRequest);
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest request)
            throws CustomAuthenticationFailedException {
        try {
            User user = userService.getUserByUsername(request.getUsername());
            if (user != null &&
                    (passwordEncoder.matches(request.getPassword(), user.getPassword()))) {
                String token = jwtProvider.generateToken(user.getUsername());
                AuthResponse authResponse = new AuthResponse(token);
                return ResponseEntity.ok(authResponse);
            }
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
        throw new CustomAuthenticationFailedException();
    }
}
