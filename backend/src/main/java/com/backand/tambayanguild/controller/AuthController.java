package com.backand.tambayanguild.controller;

import com.backand.tambayanguild.dto.AuthRequest;
import com.backand.tambayanguild.dto.AuthResponse;
import com.backand.tambayanguild.dto.LoginRequest;
import com.backand.tambayanguild.service.AuthService;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@RequestBody AuthRequest request,HttpServletRequest httpRequest) {
        AuthResponse response=auth.register(request);httpRequest.getSession(true).setAttribute("userId",response.id());return response;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request,HttpServletRequest httpRequest) {
        AuthResponse response=auth.login(request);httpRequest.getSession(true).setAttribute("userId",response.id());return response;
    }

    @PostMapping("/logout") @ResponseStatus(HttpStatus.NO_CONTENT) public void logout(HttpServletRequest request){HttpSession session=request.getSession(false);if(session!=null)session.invalidate();}
}
