package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.entity.User;

import javax.naming.AuthenticationException;

public interface UserService extends CrudService<User,Long>{
    public User authenticateUser(String nickname, String password) throws AuthenticationException;
}
