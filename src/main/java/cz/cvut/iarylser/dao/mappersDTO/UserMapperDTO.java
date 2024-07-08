package cz.cvut.iarylser.dao.mappersDTO;

import cz.cvut.iarylser.dao.DTO.UserDTO;
import cz.cvut.iarylser.dao.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class UserMapperDTO implements MapperDTO<UserDTO, User> {
    @Override
    public UserDTO toDTO(User entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setNickname(entity.getNickname());
        dto.setAge(entity.getAge());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    @Override
    public List<UserDTO> toDTOList(List<User> entities) {
        List<UserDTO> listDTO = new ArrayList<>();
        for (User user : entities) {
            listDTO.add(toDTO(user));
        }
        return listDTO;
    }
}
