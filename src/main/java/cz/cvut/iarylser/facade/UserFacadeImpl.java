package cz.cvut.iarylser.facade;

import cz.cvut.iarylser.dao.DTO.UserDTO;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.mappersDTO.UserMapperDTO;
import cz.cvut.iarylser.service.UserService;
import cz.cvut.iarylser.service.UserServiceImpl;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.util.List;
@Component
public class UserFacadeImpl implements UserFacade {
    private final UserService userService;
    private final UserMapperDTO userMapper;

    public UserFacadeImpl(UserServiceImpl userService, UserMapperDTO userMapperDTO) {
        this.userService = userService;
        this.userMapper = userMapperDTO;
    }



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
    public UserDTO update(Long id, UserDTO dto) {
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
        return authenticateUser(nickname,password);
    }
}
