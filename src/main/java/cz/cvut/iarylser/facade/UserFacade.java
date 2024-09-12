package cz.cvut.iarylser.facade;

import cz.cvut.iarylser.dao.dto.UserDTO;

import javax.naming.AuthenticationException;

public interface UserFacade extends CrudFacade<UserDTO, Long> {
    public UserDTO authenticateUser(String nickname, String password) throws AuthenticationException;
}
