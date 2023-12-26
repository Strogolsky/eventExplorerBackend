package cz.cvut.iarylser.controller;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private UserService userService;
     public UserController(UserService userService) {
         this.userService = userService;
     }
     @GetMapping
    public ResponseEntity<List<User>>getAllUsers(){
         List<User> result = userService.getAllUsers();
         return ResponseEntity.ok(result);
     }
     @PostMapping
    public ResponseEntity<User>addNewUser(@RequestBody User newUser){
         User user = userService.addNewUser(newUser);
         // todo check
         return ResponseEntity.ok(user);
     }

}
