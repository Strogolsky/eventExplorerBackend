package cz.cvut.iarylser.facade;

import cz.cvut.iarylser.dao.DTO.UserDTO;

import javax.naming.AuthenticationException;

public interface UserFacade extends CrudFacade<UserDTO, Long> {
    public UserDTO authenticateUser(String nickname, String password) throws AuthenticationException;
}
