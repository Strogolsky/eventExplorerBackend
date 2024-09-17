package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.dto.IncreaseRequest;
import cz.cvut.iarylser.dao.entity.User;

public interface UserService extends CrudService<User,Long>{
    public boolean increaseBalance(Long id, IncreaseRequest request);
}
