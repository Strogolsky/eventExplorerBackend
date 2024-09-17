package cz.cvut.iarylser.controller;
import cz.cvut.iarylser.dao.dto.IncreaseRequest;
import cz.cvut.iarylser.dao.dto.UserDTO;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.facade.UserFacadeImpl;
import cz.cvut.iarylser.service.AuthService;
import cz.cvut.iarylser.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserFacadeImpl userFacade;
    private final JwtService jwtService;
    private final AuthService authService;


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

    @PutMapping
    @Operation(summary = "Update user",
            description = "Updates the user's information for the given user ID. If the user doesn't exist, returns not found.")
    @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid user data provided for update")
    @ApiResponse(responseCode = "404", description = "User not found for the given ID")
    public ResponseEntity<?> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated user object", required = true)
            @RequestBody UserDTO updatedUser) {
        log.info("PUT request received to update user");
        User currentUser = authService.getUser();
        try {
            userFacade.update(currentUser.getId(), updatedUser);
            String newToken = jwtService.generateToken(currentUser);
            return ResponseEntity.ok(newToken);
        } catch (EntityNotFoundException e) {
            log.warn("User with id {} not found: {}", currentUser.getId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("Error updating user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    @Operation(summary = "Delete user",
            description = "Deletes a user with the specified user ID. If the user is not found, returns an error.")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found for the given ID")
    public ResponseEntity<Void> delete() {
        log.info("DELETE request received to delete user");
        User currentUser = authService.getUser();
        if (!userFacade.delete(currentUser.getId())) {
            log.warn("Unable to delete. User with id {} not found.", currentUser.getId());
            return ResponseEntity.notFound().build();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/increase")
    public ResponseEntity<Boolean> increaseBalance(@RequestBody IncreaseRequest request) throws AuthenticationException {
        log.info("Increase user balance");
        User currentUser = authService.getUser();
        return ResponseEntity.ok(userFacade.increaseBalance(currentUser.getId(), request));
    }
}
