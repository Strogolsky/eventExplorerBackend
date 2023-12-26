package cz.cvut.iarylser.controller;

import cz.cvut.iarylser.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private UserService userService;
     public UserController(UserService userService) {
         this.userService = userService;
     }
}
