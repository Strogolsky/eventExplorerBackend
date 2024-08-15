package cz.cvut.iarylser.dao.mappersDTO;

import cz.cvut.iarylser.dao.DTO.UserDTO;
import cz.cvut.iarylser.dao.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@Slf4j
public class UserMapperDTO implements MapperDTO<UserDTO, User> {
    @Override
    public UserDTO toDTO(User entity) {
        if (entity == null) {
            log.warn("Attempted to convert null User entity to DTO");
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setAge(entity.getAge());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setDescription(entity.getDescription());
        log.info("Converted User entity to DTO: {}", dto);
        return dto;
    }

    @Override
    public List<UserDTO> toDTOList(List<User> entities) {
        log.info("Converting list of User entities to DTOs, size: {}", entities.size());
        List<UserDTO> listDTO = new ArrayList<>();
        for (User user : entities) {
            listDTO.add(toDTO(user));
        }
        log.info("Converted {} User entities to DTOs", listDTO.size());
        return listDTO;
    }

    @Override
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            log.warn("Attempted to convert null UserDTO to entity");
            return null;
        }
        User entity = new User();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setAge(dto.getAge());
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setDescription(dto.getDescription());
        log.info("Converted UserDTO to entity: {}", entity);
        return entity;
    }
}
