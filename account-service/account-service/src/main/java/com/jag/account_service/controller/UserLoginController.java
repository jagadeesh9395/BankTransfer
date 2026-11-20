package com.jag.account_service.controller;

import com.jag.account_service.dto.UserLoginRegisterRequest;
import com.jag.account_service.dto.UserLoginRequest;
import com.jag.account_service.dto.UserLoginResponse;
import com.jag.account_service.service.UserLoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserLoginController {

    private final UserLoginService userLoginService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserLoginResponse> register(@Valid @RequestBody UserLoginRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userLoginService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(userLoginService.authenticate(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserLoginResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = authentication.getName();
        return ResponseEntity.ok(userLoginService.getByUsername(username));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
}
