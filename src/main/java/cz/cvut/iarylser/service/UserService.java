package cz.cvut.iarylser.service;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository repository) {
        this.userRepository = repository;
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();

    }

    public User getUserById(Long userId){
        return userRepository.findById(userId).orElse(null);
    }
    public User createUser(User newUser){
        return userRepository.save(newUser);
    }

    public User updateUser(Long userId, User updatedUser){
        User existingUser = getUserById(userId);
        // todo check
        existingUser.setNickname(updatedUser.getNickname());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setAge(updatedUser.getAge());
        existingUser.setEmail(updatedUser.getEmail());

        return userRepository.save(existingUser);
    }
    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }
}
