package com.smartstock.Controller;

import com.smartstock.Service.AuthService;
import com.smartstock.dto.LoginRequest;
import com.smartstock.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@Valid @RequestBody RegisterRequest request){
        authService.register(request);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(
            @Valid @RequestBody LoginRequest request) {

        return authService.login(request);
    }
}
