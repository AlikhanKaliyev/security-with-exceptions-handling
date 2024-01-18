package com.example.securitytrying.controller;

import com.example.securitytrying.dto.AuthRequest;
import com.example.securitytrying.dto.UserInfo;
import com.example.securitytrying.exception.LolException;
import com.example.securitytrying.repository.UserInfoRepository;
import com.example.securitytrying.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppController {
    private final UserInfoRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @GetMapping("/lol")
    public String ok() {
        if(2 == 2) throw new LolException("There is a lol error");
        return "ok";
    }

    @GetMapping("/rero")
    public String lol() {
        return "lol";
    }

    @PostMapping("/new")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfo.setRoles("ROLE"+"_"+userInfo.getRoles());
        repository.save(userInfo);
        return "user added to system ";
    }

    @PostMapping("/login")
    public String auth(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
