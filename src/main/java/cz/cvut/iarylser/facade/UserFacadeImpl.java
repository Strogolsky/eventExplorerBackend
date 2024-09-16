package cz.cvut.iarylser.facade;

import cz.cvut.iarylser.dao.dto.IncreaseRequest;
import cz.cvut.iarylser.dao.dto.UserDTO;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.mappersDto.UserMapperDTO;
import cz.cvut.iarylser.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.math.BigDecimal;
import java.util.List;
@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {
    private final UserService userService;
    private final UserMapperDTO userMapper;



    @Override
    public List<UserDTO> getAll() {
        List<User> result = userService.getAll();
        return userMapper.toDTOList(result);
    }

    @Override
    public UserDTO getById(Long id) {
        User result = userService.getById(id);
        return userMapper.toDTO(result);
    }

    @Override
    public UserDTO create(UserDTO dto) {
        User entity = userMapper.toEntity(dto);
        User result = userService.create(entity);
        return userMapper.toDTO(result);
    }

    @Override
    public UserDTO update(Long id, UserDTO dto) throws EntityNotFoundException, IllegalArgumentException {
        User entity = userMapper.toEntity(dto);
        User result = userService.update(id, entity);
        return userMapper.toDTO(result);
    }

    @Override
    public boolean delete(Long id) {
        return userService.delete(id);
    }

    @Override
    public UserDTO authenticateUser(String nickname, String password) throws AuthenticationException {
        return authenticateUser(nickname,password); //todo fix
    }
    @Override
    public boolean increaseBalance(Long id, IncreaseRequest request) {
        return userService.increaseBalance(id, request);
    }
}
