package cz.cvut.iarylser.controller;
import cz.cvut.iarylser.dao.DTO.LoginRequest;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.DTO.UserDTO;
import cz.cvut.iarylser.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users",
            description = "Retrieves a list of all users in the system.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of users",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> result = userService.getAll();
        return ResponseEntity.ok(userService.convertToDTOList(result));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get User by ID",
            description = "Retrieve a user by their unique identifier. If the user is not found, a 404 status is returned.",
            responses = {
                    @ApiResponse(description = "The User is found and returned", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(description = "User not found", responseCode = "404")
            })
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "Unique identifier of the user to be retrieved")
            @PathVariable Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            log.info("User with id {} not found.", userId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.convertToDTO(user));
    }

    @PostMapping
    @Operation(summary = "Create a new user",
            description = "Creates a new user with the provided information. Returns an error if the user cannot be added.")
    @ApiResponse(responseCode = "200", description = "User created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid user data provided")
    public ResponseEntity<?> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User object that needs to be added to the database", required = true)
            @RequestBody User newUser) {
        User user;
        try {
            user = userService.create(newUser);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body("User is not added" + e.getMessage());
        }

        return ResponseEntity.ok(userService.convertToDTO(user));
    }
    @PostMapping("/login")
    @Operation(summary = "User login",
            description = "Authenticates a user with a nickname and password. Returns the user data on success.")
    @ApiResponse(responseCode = "200", description = "User logged in successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials")
    public ResponseEntity<?> loginUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Login request with nickname and password", required = true)
            @RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getNickname();
        String password = loginRequest.getPassword();

        try {
            User user = userService.authenticateUser(username, password);
            UserDTO userDTO = userService.convertToDTO(user);
            return ResponseEntity.ok(userDTO);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PutMapping("/{userId}")
    @Operation(summary = "Update user",
            description = "Updates the user's information for the given user ID. If the user doesn't exist, returns not found.")
    @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid user data provided for update")
    @ApiResponse(responseCode = "404", description = "User not found for the given ID")
    public ResponseEntity<?> updateUser(
            @Parameter(description = "Unique identifier of the user to be retrieved")
            @PathVariable Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated user object", required = true)
            @RequestBody User updatedUser) {
        try {
            User user = userService.update(userId, updatedUser);
            if (user == null) {
                log.info("Unable to update. User with id {} not found.", userId);
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(userService.convertToDTO(user));
        } catch (IllegalArgumentException e) {
            log.info("Error updating user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user",
            description = "Deletes a user with the specified user ID. If the user is not found, returns an error.")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found for the given ID")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Unique identifier of the user to be retrieved")
            @PathVariable Long userId) {
        if (!userService.delete(userId)) {
            log.info("Unable to delete. User with id {} not found.", userId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
