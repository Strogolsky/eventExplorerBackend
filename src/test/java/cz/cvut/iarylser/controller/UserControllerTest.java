package cz.cvut.iarylser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.iarylser.dao.DTO.UserDTO;
import cz.cvut.iarylser.facade.UserFacadeImpl;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserFacadeImpl userFacade;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers() throws Exception {

        UserDTO userDTO1 = new UserDTO();
        UserDTO userDTO2 = new UserDTO();
        userDTO1.setUsername("user1");
        userDTO2.setUsername("user2");
        List<UserDTO> userDTOs = Arrays.asList(userDTO1, userDTO2);

        when(userFacade.getAll()).thenReturn(userDTOs);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nickname").value(userDTO1.getUsername()))
                .andExpect(jsonPath("$[1].nickname").value(userDTO2.getUsername()));
    }

    @Test
    void getUserByIdSucceeded() throws Exception {
        Long userId = 1L;
        String userNickname = "user1";

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername(userNickname);

        when(userFacade.getById(userId)).thenReturn(userDTO);

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.nickname").value(userDTO.getUsername()));
    }

    @Test
    void getUserByIdFailure() throws Exception {
        Long userId = 1L;
        when(userFacade.getById(userId)).thenReturn(null);

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUserSuccesful() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setAge(25);
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setDescription("Test description");

        when(userFacade.create(any(UserDTO.class))).thenReturn(userDTO);

        String userJson = new ObjectMapper().writeValueAsString(userDTO);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(userDTO.getUsername()))
                .andExpect(jsonPath("$.age").value(userDTO.getAge()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.firstName").value(userDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDTO.getLastName()))
                .andExpect(jsonPath("$.description").value(userDTO.getDescription()));
    }

    @Test
    public void createUserFail() throws Exception {
        UserDTO userDTO = new UserDTO();
        when(userFacade.create(any(UserDTO.class))).thenThrow(new IllegalArgumentException());

        String userJson = new ObjectMapper().writeValueAsString(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserSucceeded() throws Exception {
        Long userId = 1L;

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(userId);
        updatedUserDTO.setUsername("updatedTest");
        updatedUserDTO.setEmail("updated@example.com");
        updatedUserDTO.setFirstName("UpdatedFirstName");
        updatedUserDTO.setLastName("UpdatedLastName");
        updatedUserDTO.setDescription("Updated description");
        updatedUserDTO.setAge(30);

        Mockito.when(userFacade.update(userId, updatedUserDTO)).thenReturn(updatedUserDTO);

        String updatedUserJson = new ObjectMapper().writeValueAsString(updatedUserDTO);

        mockMvc.perform(put("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(updatedUserDTO.getUsername()))
                .andExpect(jsonPath("$.email").value(updatedUserDTO.getEmail()))
                .andExpect(jsonPath("$.firstName").value(updatedUserDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedUserDTO.getLastName()))
                .andExpect(jsonPath("$.description").value(updatedUserDTO.getDescription()))
                .andExpect(jsonPath("$.age").value(updatedUserDTO.getAge()));

    }

    @Test
    void updateUserFailureNickname() throws Exception {
        Long userId = 1L;
        UserDTO updatedUserDTO = new UserDTO();
        given(userFacade.update(eq(userId), any(UserDTO.class))).willThrow(new IllegalArgumentException());

        mockMvc.perform(put("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserFailureNotFound() throws Exception {
        Long userId = 1L;
        UserDTO updatedUserDTO = new UserDTO();
        given(userFacade.update(eq(userId), any(UserDTO.class))).willReturn(null);

        mockMvc.perform(put("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedUserDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUserSucceeded() throws Exception {
        Long userId = 1L;
        Mockito.when(userFacade.delete(userId)).thenReturn(true);

        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserFailure() throws Exception {
        Long userId = 1L;
        given(userFacade.delete(userId)).willReturn(false);

        mockMvc.perform(delete("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}