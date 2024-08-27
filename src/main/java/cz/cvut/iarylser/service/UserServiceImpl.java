package cz.cvut.iarylser.service;
import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.Role;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EventServiceImpl eventService;

    @Override
    @Cacheable(value = "users")
    public List<User> getAll() {
        log.info("Fetching all users");
        return userRepository.findAll();

    }
    @Override
    @Cacheable(value = "users", key = "#userId")
    public User getById(Long userId) {
        log.info("Fetching user with id: {}", userId);
        return userRepository.findById(userId).orElse(null);
    }
    @Override
    public User create(User newUser) throws IllegalArgumentException{
        log.info("Creating new user with nickname: {}", newUser.getUsername());
        if(userRepository.existsByUsername(newUser.getUsername())) {
            log.warn("User with username {} already exists", newUser.getUsername());
            throw new IllegalArgumentException("User with this username already exists");
        }
        log.info("User is created");
        return userRepository.save(newUser);
    }

    @Override
    @CachePut(value = "users", key = "#userId")
    public User update(Long userId, User updatedUser) throws EntityNotFoundException, IllegalArgumentException{
        log.info("Updating user with id: {}", userId);
        User existingUser = getById(userId);
        if (existingUser == null) {
            log.warn("User with id {} not found for update", userId);
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }

        if(userRepository.existsByUsername(updatedUser.getUsername())) {
            log.warn("User with nickname {} already exists", updatedUser.getUsername());
            throw new IllegalArgumentException("User with this nickname already exists");
        }

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(updatedUser.getPassword());
        }
        if (updatedUser.getAge() != 0) {
            existingUser.setAge(updatedUser.getAge());
        }
        existingUser.setEmail(updatedUser.getEmail());

        updateUserEvents(existingUser);

        log.info("User with id: {} is updated", userId);
        return userRepository.save(existingUser);
    }

    private void updateUserEvents(User updatedUser) {
        log.info("Updating events for user with id: {}", updatedUser.getId());
        List<Event> events = updatedUser.getCreatedEvents();
        for (Event event : events) {
            eventService.updateForOrgChange(event, updatedUser);
        }
    }
    @Override
    @CacheEvict(value = "users", key = "#userId")
    public boolean delete(Long userId) {
        log.info("Deleting user with id: {}", userId);
        if (!userRepository.existsById(userId)) {
            log.warn("User with id {} not found for deletion", userId);
            return false;
        }
        userRepository.deleteById(userId);
        log.info("Deleted user with id: {}", userId);
        return true;
    }

    public User getByUsername(String username) throws EntityNotFoundException{
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.warn("User with username {} not found for update", username);
            throw new EntityNotFoundException("User with username " + username + " not found");
        }
        return user;
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    @Deprecated
    public void getAdmin() {
        User user = getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
    }
}
