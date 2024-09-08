package cz.cvut.iarylser.unit.dao.repository;

import cz.cvut.iarylser.dao.entity.Role;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    User user;
    @BeforeEach
    void setUp(){
        user = new User();
        user.setId(1L);
        user.setUsername("NoizeMC");
        user.setAge(38);
        user.setPassword("1234567");
        user.setEmail("Ivan@example.com");
        user.setRole(Role.ROLE_ADMIN);
    }

    @Test
    void findByUsernameSucceeded() {
        //give
        userRepository.save(user);
        String nickname = "NoizeMC";
        //when
        User found = userRepository.findByUsername(nickname);
        //then
        assertNotNull(found);
        assertEquals(found.getUsername(), user.getUsername());
    }
    @Test
    void findByUsernameFailure() {
        //give
        userRepository.save(user);
        String nickname = "unknowedUser";
        //when
        User found = userRepository.findByUsername(nickname);
        //then
        assertNull(found);
    }
}