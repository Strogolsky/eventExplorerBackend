package cz.cvut.iarylser.dao.repository;

import cz.cvut.iarylser.dao.entity.User;
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
        user.setNickname("NoizeMC");
        user.setAge(38);
        user.setPassword("1234567");
        user.setEmail("Ivan@example.com");
    }

    @Test
    void findByNicknameSucceeded() {
        //give
        userRepository.save(user);
        String nickname = "NoizeMC";
        //when
        User found = userRepository.findByNickname(nickname);
        //then
        assertNotNull(found);
        assertEquals(found.getNickname(), user.getNickname());
    }
    @Test
    void findByNicknameFailure() {
        //give
        userRepository.save(user);
        String nickname = "unknowedUser";
        //when
        User found = userRepository.findByNickname(nickname);
        //then
        assertNull(found);
    }
}