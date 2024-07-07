package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.DTO.UserDTO;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.repository.UserRepository;
import cz.cvut.iarylser.service.EventService;
import cz.cvut.iarylser.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.naming.AuthenticationException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EventService eventService;

    @Autowired
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

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();
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
        newUser.setNickname("existing_nickname");
        when(userRepository.existsByNickname(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.create(newUser);
        });
    }
    @Test
    void updateUserFailure() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User result = userService.update(userId, new User());

        assertNull(result);
    }

    @Test
    void updateUserSucceeded() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setNickname("OldNickname");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setNickname("NewNickname");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.update(userId, updatedUser);

        assertNotNull(result);
        assertEquals("NewNickname", result.getNickname());
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
    void convertToDTO() {
        User user = new User();
        user.setNickname("TestUser");
        user.setAge(30);
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setDescription("Test Description");

        UserDTO dto = userService.convertToDTO(user);

        assertNotNull(dto);
        assertEquals("TestUser", dto.getNickname());
        assertEquals(30, dto.getAge());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("Test", dto.getFirstName());
        assertEquals("User", dto.getLastName());
        assertEquals("Test Description", dto.getDescription());
    }

    @Test
    void convertToDTOList() {
        user1.setAge(25);
        user1.setEmail("user1@example.com");
        user1.setFirstName("FirstName1");
        user1.setLastName("LastName1");
        user1.setDescription("Description1");

        user2.setAge(30);
        user2.setEmail("user2@example.com");
        user2.setFirstName("FirstName2");
        user2.setLastName("LastName2");
        user2.setDescription("Description2");

        List<User> users = Arrays.asList(user1, user2);

        List<UserDTO> dtos = userService.convertToDTOList(users);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());

        UserDTO dto1 = dtos.get(0);
        assertEquals("user1", dto1.getNickname());
        assertEquals(25, dto1.getAge());
        assertEquals("user1@example.com", dto1.getEmail());
        assertEquals("FirstName1", dto1.getFirstName());
        assertEquals("LastName1", dto1.getLastName());
        assertEquals("Description1", dto1.getDescription());

        UserDTO dto2 = dtos.get(1);
        assertEquals("user2", dto2.getNickname());
        assertEquals(30, dto2.getAge());
        assertEquals("user2@example.com", dto2.getEmail());
        assertEquals("FirstName2", dto2.getFirstName());
        assertEquals("LastName2", dto2.getLastName());
        assertEquals("Description2", dto2.getDescription());
    }

    @Test
    public void authenticateUserSucceeds() throws AuthenticationException {
        String nickname = "testUser";
        String password = "testPassword";
        User mockUser = new User();
        mockUser.setNickname(nickname);
        mockUser.setPassword(password);

        when(userRepository.findByNickname(nickname)).thenReturn(mockUser);

        User result = userService.authenticateUser(nickname, password);

        assertNotNull(result);
        assertEquals(nickname, result.getNickname());
        assertEquals(password, result.getPassword());
    }

    @Test
    void updateUserFailureNicknameExist(){
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setNickname("originalNickname");

        User updatedUser = new User();
        updatedUser.setNickname("existingNickname");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByNickname("existingNickname")).thenReturn(true);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> userService.update(userId, updatedUser),
                "Expected updateUser to throw, but it didn't"
        );

    }
    @Test
    void authenticateUserFailure() {
        String nickname = "nonexistent_user";
        when(userRepository.findByNickname(nickname)).thenReturn(null);

        assertThrows(AuthenticationException.class, () -> {
            userService.authenticateUser(nickname, "password");
        });
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