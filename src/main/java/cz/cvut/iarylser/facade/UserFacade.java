package cz.cvut.iarylser.facade;

import cz.cvut.iarylser.dao.dto.IncreaseRequest;
import cz.cvut.iarylser.dao.dto.UserDTO;

import javax.naming.AuthenticationException;

public interface UserFacade extends CrudFacade<UserDTO, Long> {
    public UserDTO authenticateUser(String nickname, String password) throws AuthenticationException;
    public boolean increaseBalance(Long id, IncreaseRequest request) throws AuthenticationException;
}
