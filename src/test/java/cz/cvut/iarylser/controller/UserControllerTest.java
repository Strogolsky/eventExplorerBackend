package cz.cvut.iarylser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

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
    void getUserById() throws Exception {
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
    void createUser() throws Exception{
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

        when(userService.createUser(Mockito.any(User.class))).thenReturn(user);
        when(userService.convertToDTO(Mockito.any(User.class))).thenReturn(userDTO);


        user.setNickname("a;sdasd");

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
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}