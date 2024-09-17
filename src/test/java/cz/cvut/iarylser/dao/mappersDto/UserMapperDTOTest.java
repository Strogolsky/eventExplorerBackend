package cz.cvut.iarylser.dao.mappersDto;

import cz.cvut.iarylser.dao.dto.UserDTO;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.mappersDto.UserMapperDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperDTOTest {
    private UserMapperDTO userMapperDTO;
    private User entity;
    private UserDTO dto;
    private List<UserDTO> dtoList;
    private List<User> userList;
    @BeforeEach
    void setUp() {
        userMapperDTO = new UserMapperDTO();
        entity = new User();
        userList = new ArrayList<>();
        dtoList = new ArrayList<>();

        entity.setId(1L);
        entity.setUsername("username");
        entity.setEmail("email");
        entity.setFirstName("firstName");
        entity.setLastName("lastName");
        entity.setDescription("description");

        dto = new UserDTO();
        dto.setId(1L);
        dto.setUsername("username");
        dto.setEmail("email");
        dto.setFirstName("firstName");
        dto.setLastName("lastName");
        dto.setDescription("description");

        userList.add(entity);
        dtoList.add(dto);

        userList.add(entity);
        dtoList.add(dto);

    }
    @Test
    void toDTO() {
        UserDTO newDto = userMapperDTO.toDTO(entity);
        assertEquals(newDto,dto);
    }

    @Test
    void toDTOList() {
        List<UserDTO> newDtoList = userMapperDTO.toDTOList(userList);
        assertEquals(newDtoList,dtoList);
    }

    @Test
    void toEntity() {
        User newEntity = userMapperDTO.toEntity(dto);
        assertEquals(newEntity,entity);
    }
}