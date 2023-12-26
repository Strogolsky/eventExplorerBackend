package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    public void createUser(String nickname, String email, int age){
        // todo logic
        User newUser = new User(nickname,age, email);
        repository.save(newUser);
    }
    public void createEvent(){
        Event newEvent = new Event();
    }
}
