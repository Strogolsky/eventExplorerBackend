package cz.cvut.iarylser.controller;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.service.TicketService;
import cz.cvut.iarylser.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private UserService userService;
     public UserController(UserService userService, TicketService ticketService) {
         this.userService = userService;
     }
     @GetMapping
    public ResponseEntity<List<User>>getAllUsers(){
         List<User> result = userService.getAllUsers();
         return ResponseEntity.ok(result);
     }
     @GetMapping("/{userId}")
     public ResponseEntity<User>getUserById(@PathVariable Long userId){
         User user = userService.getUserById(userId);
         // todo check on null
         return ResponseEntity.ok(user);
     }
     @PostMapping
    public ResponseEntity<User>createUser(@RequestBody User newUser){
         User user = userService.createUser(newUser);
         // todo check
         return ResponseEntity.ok(user);
     }
     @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User updatedUser){
         User user = userService.updateUser(userId, updatedUser);
         // todo check
         return ResponseEntity.ok(user);
     }
     @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
         userService.deleteUser(userId);
         return ResponseEntity.noContent().build();
     }

}
