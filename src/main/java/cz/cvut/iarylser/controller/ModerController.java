package cz.cvut.iarylser.controller;

import cz.cvut.iarylser.service.ModerServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/moderator")
@RequiredArgsConstructor
@Slf4j
public class ModerController {
    private final ModerServiceImpl moderService;

    @PutMapping("/user/block/{userId}")
    public ResponseEntity<?> blockUser(@PathVariable Long userId) {
        try {
            moderService.blockUser(userId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/user/unblock/{userId}")
    public ResponseEntity<?> unblockUser(@PathVariable Long userId) {
        try {
            moderService.blockUser(userId);
            return  ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
