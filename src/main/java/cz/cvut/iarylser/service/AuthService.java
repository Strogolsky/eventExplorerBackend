package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.dto.JwtAuthResponse;
import cz.cvut.iarylser.dao.dto.SignInRequest;
import cz.cvut.iarylser.dao.dto.SignUpRequest;
import cz.cvut.iarylser.dao.entity.Role;
import cz.cvut.iarylser.dao.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserServiceImpl userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtAuthResponse signUp(SignUpRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setRole(Role.ROLE_USER);

        userService.create(user);
        var jwt = jwtService.generateToken(user);
        return new JwtAuthResponse(jwt);
    }

    public JwtAuthResponse signIn(SignInRequest request) {
        log.info("Authenticating {}", request.getUsername());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        log.info("Authentication success");
        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());
        var jwt = jwtService.generateToken(user);
        return new JwtAuthResponse(jwt);
    }
    public User getUser(){
        log.info("Authentification user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User CurrentUser = (User) authentication.getPrincipal();
        log.info("Current user with ID: {}", CurrentUser.getId());
        return CurrentUser;
    }
}
