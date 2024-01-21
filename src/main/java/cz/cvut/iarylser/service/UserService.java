package cz.cvut.iarylser.service;
import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.DTO.UserDTO;
import cz.cvut.iarylser.dao.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final EventService eventService;

    public UserService(UserRepository repository, EventService eventService) {
        this.userRepository = repository;
        this.eventService = eventService;
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();

    }

    public User getUserById(Long userId){
        return userRepository.findById(userId).orElse(null);
    }
    public User createUser(User newUser){
        if(userRepository.existsByNickname(newUser.getNickname())){
            throw new IllegalArgumentException();
        }
        return userRepository.save(newUser);
    }


    public User updateUser(Long userId, User updatedUser) {
        User existingUser = getUserById(userId);
        if (existingUser == null) {
            log.warn("User with id {} not found for update", userId);
            return null;
        }

        if(userRepository.existsByNickname(updatedUser.getNickname())){
            throw new IllegalArgumentException();
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


        return userRepository.save(existingUser);
    }

    private void updateUserEvents(User updatedUser) {
        List<Event> events = updatedUser.getCreatedEvents();
        for (Event event : events) {
            eventService.updateForOrgChange(event, updatedUser);
        }
    }
    public boolean deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("User with id {} not found for deletion", userId);
            return false;
        }
        userRepository.deleteById(userId);
        return true;
    }

    public User authenticateUser(String nickname, String password) throws AuthenticationException {
        User user = userRepository.findByNickname(nickname);

        if (user == null) {
            throw new AuthenticationException();
        }

        if (!user.getPassword().equals(password)) {
            throw new AuthenticationException();
        }
        return user;
    }

    public UserDTO convertToDTO(User user){
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setNickname(user.getNickname());
        dto.setAge(user.getAge());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDescription(user.getDescription());
        return dto;
    }
    public List<UserDTO> convertToDTOList(List<User> users) {
        List<UserDTO> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(convertToDTO(user));
        }
        return dtos;
    }
}
