package cz.cvut.iarylser.helpers;

import cz.cvut.iarylser.dao.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthHelper {
    public User authenticationUser(){
        log.info("Authentification user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User CurrentUser = (User) authentication.getPrincipal();
        log.info("Current user with ID: {}", CurrentUser.getId());
        return CurrentUser;
    }
}
