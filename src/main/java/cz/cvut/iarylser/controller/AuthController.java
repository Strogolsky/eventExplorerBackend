package cz.cvut.iarylser.controller;

import cz.cvut.iarylser.dao.DTO.JwtAuthenticationResponse;
import cz.cvut.iarylser.dao.DTO.SignInRequest;
import cz.cvut.iarylser.dao.DTO.SignUpRequest;
import cz.cvut.iarylser.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        log.info("POST request for sign up");
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        log.info("POST request for sign in");
        return authenticationService.signIn(request);
    }
}
