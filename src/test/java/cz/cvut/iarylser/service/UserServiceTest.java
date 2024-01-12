package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventService eventService;

    @InjectMocks
    private UserService userService;

    private User user1, user2;


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

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getNickname());
        assertEquals("user2", result.get(1).getNickname());
    }

    @Test
    void getUserById() {
        //given
        Long id = 1L;

        //when
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user1));
        User found = userService.getUserById(id);

        //then
        assertNotNull(found);
        assertEquals(found.getId(),1L);
    }

    @Test
    void createUser() {
        // when
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user1);
        User result = userService.createUser(user1);
        //then
        assertEquals(user1, result);
        Mockito.verify(userRepository).save(user1);
    }

    @Test
    void updateUser() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setNickname("OldNickname");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setNickname("NewNickname");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(userId, updatedUser);

        assertNotNull(result);
        assertEquals("NewNickname", result.getNickname());
        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(userRepository).save(existingUser);
    }

    @Test
    void deleteUser() {
    }

    @Test
    void convertToDTO() {
    }

    @Test
    void convertToDTOList() {
    }

    private void initUsers(){
        user1 = new User();
        user2 = new User();

        user1.setId(1L);
        user1.setNickname("user1");

        user2.setId(2l);
        user2.setNickname("user2");
    }
}