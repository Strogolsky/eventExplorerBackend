package cz.cvut.iarylser.controller;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.DTO.UserDTO;
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
    public ResponseEntity<List<UserDTO>>getAllUsers(){
         List<User> result = userService.getAllUsers();
         return ResponseEntity.ok(userService.convertToDTOList(result));
     }
     @GetMapping("/{userId}")
     public ResponseEntity<UserDTO>getUserById(@PathVariable Long userId){
         User user = userService.getUserById(userId);
         // todo check on null
         return ResponseEntity.ok(userService.convertToDTO(user));
     }
     @PostMapping
    public ResponseEntity<UserDTO>createUser(@RequestBody User newUser){
         User user = userService.createUser(newUser);
         // todo check
         return ResponseEntity.ok(userService.convertToDTO(user));
     }
     @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody User updatedUser){
         User user = userService.updateUser(userId, updatedUser);
         // todo check
         return  ResponseEntity.ok(userService.convertToDTO(user));
     }
     @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
         userService.deleteUser(userId);
         return ResponseEntity.noContent().build();
     }

}
