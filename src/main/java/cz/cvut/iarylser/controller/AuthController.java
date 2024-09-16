package cz.cvut.iarylser.controller;
import cz.cvut.iarylser.dao.dto.JwtAuthenticationResponse;
import cz.cvut.iarylser.dao.dto.SignInRequest;
import cz.cvut.iarylser.dao.dto.SignUpRequest;
import cz.cvut.iarylser.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
    public JwtAuthenticationResponse signUp(@RequestBody @Validated SignUpRequest request) {
        log.info("POST request for sign up");
        return authService.signUp(request);
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Sign in")
    @ApiResponse(responseCode = "200", description = "Successful sign in")
    @ApiResponse(responseCode = "403", description = "Incorrect username or password")
    public JwtAuthenticationResponse signIn(@RequestBody @Validated SignInRequest request) {
        log.info("POST request for sign in");
        return authService.signIn(request);
    }
}
