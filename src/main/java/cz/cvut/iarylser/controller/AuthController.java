package cz.cvut.iarylser.controller;
import cz.cvut.iarylser.dao.DTO.JwtAuthenticationResponse;
import cz.cvut.iarylser.dao.DTO.SignInRequest;
import cz.cvut.iarylser.dao.DTO.SignUpRequest;
import cz.cvut.iarylser.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private final AuthService authService;

    @PostMapping("/sign-up")
    @Operation(summary = "Sign up")
    @ApiResponse(responseCode = "200", description = "Successful sign up")
    @ApiResponse(responseCode = "403", description = "This user already exists")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        log.info("POST request for sign up");
        return authService.signUp(request);
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Sign in")
    @ApiResponse(responseCode = "200", description = "Successful sign in")
    @ApiResponse(responseCode = "403", description = "Incorrect username or password")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        log.info("POST request for sign in");
        return authService.signIn(request);
    }
}
