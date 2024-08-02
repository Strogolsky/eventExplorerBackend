package cz.cvut.iarylser.service;
import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EventService eventService;

    public UserServiceImpl(UserRepository repository, EventServiceImpl eventService) {
        this.userRepository = repository;
        this.eventService = eventService;
    }
    @Override
    public List<User> getAll() {
        log.info("Fetching all users");
        return userRepository.findAll();

    }
    @Override
    public User getById(Long userId) {
        log.info("Fetching user with id: {}", userId);
        return userRepository.findById(userId).orElse(null);
    }
    @Override
    public User create(User newUser) throws IllegalArgumentException{
        log.info("Creating new user with nickname: {}", newUser.getNickname());
        if(userRepository.existsByNickname(newUser.getNickname())) {
            log.warn("User with nickname {} already exists", newUser.getNickname());
            throw new IllegalArgumentException("User with this nickname already exists");
        }
        log.info("User is created");
        return userRepository.save(newUser);
    }

    @Override
    public User update(Long userId, User updatedUser) throws EntityNotFoundException, IllegalArgumentException{
        log.info("Updating user with id: {}", userId);
        User existingUser = getById(userId);
        if (existingUser == null) {
            log.warn("User with id {} not found for update", userId);
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }

        if(userRepository.existsByNickname(updatedUser.getNickname())) {
            log.warn("User with nickname {} already exists", updatedUser.getNickname());
            throw new IllegalArgumentException("User with this nickname already exists");
        }

        if (updatedUser.getNickname() != null && !updatedUser.getNickname().isEmpty()) {
            existingUser.setNickname(updatedUser.getNickname());
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
    @Override
    public User authenticateUser(String nickname, String password) throws AuthenticationException {
        log.info("Authenticating user with nickname: {}", nickname);
        User user = userRepository.findByNickname(nickname);

        if (user == null) {
            log.warn("Authentication failed: user with nickname {} not found", nickname);
            throw new AuthenticationException("User not found");
        }

        if (!user.getPassword().equals(password)) {
            log.warn("Authentication failed: incorrect password for user with nickname {}", nickname);
            throw new AuthenticationException("Incorrect password");
        }
        log.info("User authenticated successfully");
        return user;
    }
}
