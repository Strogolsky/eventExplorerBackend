package cz.cvut.iarylser.controller;
import cz.cvut.iarylser.dao.DTO.LoginRequest;
import cz.cvut.iarylser.dao.DTO.UserDTO;
import cz.cvut.iarylser.facade.UserFacade;
import cz.cvut.iarylser.facade.UserFacadeImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.action.internal.EntityActionVetoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/users")
@Slf4j
public class UserController {
    private final UserFacade userFacade;

    public UserController(UserFacadeImpl userFacade) {
        this.userFacade = userFacade;
    }

    @GetMapping
    @Operation(summary = "Get all users",
            description = "Retrieves a list of all users in the system.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of users",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    public ResponseEntity<List<UserDTO>> getAll() {
        log.info("GET request received to retrieve all users.");
        List<UserDTO> result = userFacade.getAll();
        return ResponseEntity.ok(result);
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
    public ResponseEntity<UserDTO> getById(
            @Parameter(description = "Unique identifier of the user to be retrieved")
            @PathVariable Long userId) {
        log.info("GET request received to retrieve user with ID: {}", userId);
        UserDTO result = userFacade.getById(userId);
        if (result == null) {
            log.info("User with id {} not found.", userId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Create a new user",
            description = "Creates a new user with the provided information. Returns an error if the user cannot be added.")
    @ApiResponse(responseCode = "200", description = "User created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid user data provided")
    public ResponseEntity<?> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User object that needs to be added to the database", required = true)
            @RequestBody UserDTO newUser) {
        UserDTO result;
        log.info("POST request received to create a new user.");
        try {
            result = userFacade.create(newUser);
        } catch (IllegalArgumentException e){
            log.warn("Error creating user: {}", e.getMessage());
            return ResponseEntity.badRequest().body("User is not added" + e.getMessage());
        }
        log.info("User is created");
        return ResponseEntity.ok(result);
    }
    @PostMapping("/login")
    @Operation(summary = "User login",
            description = "Authenticates a user with a nickname and password. Returns the user data on success.")
    @ApiResponse(responseCode = "200", description = "User logged in successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials")
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Login request with nickname and password", required = true)
            @RequestBody LoginRequest loginRequest) {
        log.info("POST request received for user login with nickname: {}", loginRequest.getNickname());
        String username = loginRequest.getNickname();
        String password = loginRequest.getPassword();

        try {
            UserDTO result = userFacade.authenticateUser(username, password);
            return ResponseEntity.ok(result);
        } catch (AuthenticationException e) {
            log.info("Authentication failed for user with nickname: {}", username);
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
    public ResponseEntity<?> update(
            @Parameter(description = "Unique identifier of the user to be retrieved")
            @PathVariable Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated user object", required = true)
            @RequestBody UserDTO updatedUser) {
        log.info("PUT request received to update user with ID: {}", userId);
        try {
            UserDTO result = userFacade.update(userId, updatedUser);
            return ResponseEntity.ok(result);
        } catch (EntityNotFoundException e) {
            log.warn("User with id {} not found: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("Error updating user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user",
            description = "Deletes a user with the specified user ID. If the user is not found, returns an error.")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found for the given ID")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Unique identifier of the user to be retrieved")
            @PathVariable Long userId) {
        log.info("DELETE request received to delete user with ID: {}", userId);
        if (!userFacade.delete(userId)) {
            log.warn("Unable to delete. User with id {} not found.", userId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
