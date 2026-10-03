package com.smartstock.Service;

import com.smartstock.Repo.UserRepo;
import com.smartstock.dto.LoginRequest;
import com.smartstock.dto.RegisterRequest;
import com.smartstock.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthService(UserRepo userRepo,PasswordEncoder passwordEncoder,JwtService jwtService){
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request){
        if(userRepo.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already registered...");
        }
        User user = new User();

        user.setEmail(request.getEmail());
        user.setName(request.getName());
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);
        user.setRole("USER");
        userRepo.save(user);
    }

    public String login(LoginRequest request) {

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid email or password");
        }

        return jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );
    }
}

