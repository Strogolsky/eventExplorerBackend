package cz.cvut.iarylser.Unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.iarylser.controller.UserController;
import cz.cvut.iarylser.dao.DTO.LoginRequest;
import cz.cvut.iarylser.dao.DTO.UserDTO;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.AuthenticationException;
@WebMvcTest(UserController.class)
class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers() throws Exception {
        User user1 = new User();
        User user2 = new User();
        user1.setNickname("user1");
        user2.setNickname("user2");
        List<User> users = Arrays.asList(user1, user2);

        UserDTO userDTO1 = new UserDTO();
        UserDTO userDTO2 = new UserDTO();
        userDTO1.setNickname("user1");
        userDTO2.setNickname("user2");
        List<UserDTO> userDTOs = Arrays.asList(userDTO1, userDTO2);

        when(userService.getAllUsers()).thenReturn(users);
        when(userService.convertToDTOList(users)).thenReturn(userDTOs);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nickname").value(user1.getNickname()))
                .andExpect(jsonPath("$[1].nickname").value(user2.getNickname()));
    }

    @Test
    void getUserByIdSucceeded() throws Exception {
        Long userId = 1L;
        String userNickname = "user1";

        User user = new User();
        user.setId(userId);
        user.setNickname(userNickname);

        UserDTO userDTO = new UserDTO();
        userDTO.setNickname(userNickname);

        when(userService.getUserById(userId)).thenReturn(user);
        when(userService.convertToDTO(user)).thenReturn(userDTO);

        mockMvc.perform(get("/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(user.getNickname()));
    }
    @Test
    void getUserByIdFailure() throws Exception {
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(null);

        mockMvc.perform(get("/user/{userId}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUserSuccesful() throws Exception{
        User user = new User();
        user.setNickname("testUser");
        user.setAge(25);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDescription("Test description");

        UserDTO userDTO = new UserDTO();
        userDTO.setNickname(user.getNickname());
        userDTO.setAge(user.getAge());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setDescription(user.getDescription());

        when(userService.createUser(any(User.class))).thenReturn(user);
        when(userService.convertToDTO(any(User.class))).thenReturn(userDTO);

        String userJson = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(userDTO.getNickname()))
                .andExpect(jsonPath("$.age").value(userDTO.getAge()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.firstName").value(userDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDTO.getLastName()))
                .andExpect(jsonPath("$.description").value(userDTO.getDescription()));
    }

    @Test
    public void createUserFail() throws Exception {
        User user = new User();
        when(userService.createUser(any(User.class))).thenThrow(new IllegalArgumentException());

        String userJson = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserSucceeded() throws Exception {
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setNickname("updatedTest");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setFirstName("UpdatedFirstName");
        updatedUser.setLastName("UpdatedLastName");
        updatedUser.setDescription("Updated description");
        updatedUser.setAge(30);

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setNickname(updatedUser.getNickname());
        updatedUserDTO.setEmail(updatedUser.getEmail());
        updatedUserDTO.setFirstName(updatedUser.getFirstName());
        updatedUserDTO.setLastName(updatedUser.getLastName());
        updatedUserDTO.setDescription(updatedUser.getDescription());
        updatedUserDTO.setAge(updatedUser.getAge());

        Mockito.when(userService.updateUser(userId, updatedUser)).thenReturn(updatedUser);
        Mockito.when(userService.convertToDTO(updatedUser)).thenReturn(updatedUserDTO);

        String updatedUserJson = new ObjectMapper().writeValueAsString(updatedUser);

        mockMvc.perform(put("/user/" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatedUserJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(updatedUserDTO.getNickname()))
                .andExpect(jsonPath("$.email").value(updatedUserDTO.getEmail()))
                .andExpect(jsonPath("$.firstName").value(updatedUserDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedUserDTO.getLastName()))
                .andExpect(jsonPath("$.description").value(updatedUserDTO.getDescription()))
                .andExpect(jsonPath("$.age").value(updatedUserDTO.getAge()));

    }

    @Test
    void updateUserFailureNickname() throws Exception {
        Long userId = 1L;
        User updatedUser = new User();
        given(userService.updateUser(eq(userId), any(User.class))).willThrow(new IllegalArgumentException());

        mockMvc.perform(put("/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserFailureNotFound() throws Exception {
        Long userId = 1L;
        User updatedUser = new User();
        given(userService.updateUser(eq(userId), any(User.class))).willReturn(null);

        mockMvc.perform(put("/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUserSucceeded() throws Exception {
        Long userId = 1L;
        Mockito.when(userService.deleteUser(userId)).thenReturn(true);

        mockMvc.perform(delete("/user/" + userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserFailure() throws Exception {
        Long userId = 1L;
        given(userService.deleteUser(userId)).willReturn(false);

        mockMvc.perform(delete("/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void loginUserSucceeds() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");
        UserDTO userDTO = new UserDTO(1L, "testUser", 25, "test@example.com", "FirstName", "LastName", "Description");

        when(userService.authenticateUser(anyString(), anyString())).thenReturn(new User());
        when(userService.convertToDTO(any(User.class))).thenReturn(userDTO);


        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.nickname").value(userDTO.getNickname()))
                .andExpect(jsonPath("$.age").value(userDTO.getAge()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.firstName").value(userDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDTO.getLastName()))
                .andExpect(jsonPath("$.description").value(userDTO.getDescription()));
    }

    @Test
    void loginUserFailure() throws Exception {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        when(userService.authenticateUser(anyString(), anyString())).thenThrow(new AuthenticationException());


        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}