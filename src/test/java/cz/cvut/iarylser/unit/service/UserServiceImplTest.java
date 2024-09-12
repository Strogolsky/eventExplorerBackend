package cz.cvut.iarylser.unit.service;

import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.mappersDto.UserMapperDTO;
import cz.cvut.iarylser.dao.repository.UserRepository;
import cz.cvut.iarylser.service.EventServiceImpl;
import cz.cvut.iarylser.service.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = UserServiceImpl.class)
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;
    private UserMapperDTO userMapperDTO;

    @MockBean
    private EventServiceImpl eventServiceImpl;

    @Autowired
    private UserServiceImpl userService;

    private User user1, user2;
    @Autowired
    private UserServiceImpl userServiceImpl;


    @BeforeEach
    void setUp() {
        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
        initUsers();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllUsers() {
        List<User> users = Arrays.asList(user1,user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());

        Mockito.verify(userRepository).findAll();
    }

    @Test
    void getUserById() {
        //given
        Long id = 1L;

        //when
        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user1));
        User found = userService.getById(id);

        //then
        assertNotNull(found);
        assertEquals(found.getId(),1L);
    }

    @Test
    void createUserSucceeded() {
        // when
        when(userRepository.save(any(User.class))).thenReturn(user1);
        User result = userService.create(user1);
        //then
        assertEquals(user1, result);
        Mockito.verify(userRepository).save(user1);
    }

    @Test
    void createUserFailure() {
        User newUser = new User();
        newUser.setUsername("existing_nickname");
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.create(newUser);
        });
    }
    @Test
    void UpdateUserNotFoundTest() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.update(userId, new User()));
    }

    @Test
    void updateUserSucceeded() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("OldNickname");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername("NewNickname");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.update(userId, updatedUser);

        assertNotNull(result);
        assertEquals("NewNickname", result.getUsername());
        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(userRepository).save(existingUser);
    }

    @Test
    void deleteUserSucceeded() {
        // given
        Long userId = 1L;
        Mockito.when(userRepository.existsById(userId)).thenReturn(true);

        // when
        boolean result = userService.delete(userId);

        //then
        assertTrue(result);
        Mockito.verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteUserFailure() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        boolean result = userService.delete(userId);

        assertFalse(result);
    }

    @Test
    void updateUserFailureNicknameExist(){
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("originalNickname");

        User updatedUser = new User();
        updatedUser.setUsername("existingNickname");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("existingNickname")).thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.update(userId, updatedUser),
                "Expected updateUser to throw, but it didn't"
        );

    }

    private void initUsers(){
        user1 = new User();
        user2 = new User();

        user1.setId(1L);
        user1.setUsername("user1");

        user2.setId(2l);
        user2.setUsername("user2");
    }
}