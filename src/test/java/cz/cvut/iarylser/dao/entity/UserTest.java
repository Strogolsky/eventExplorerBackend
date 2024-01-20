package cz.cvut.iarylser.dao.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Event event1;
    private Event event2;
    @BeforeEach
    void setUp() {
        user = new User();
        event1 = new Event();
        event2 = new Event();
    }

    @Test
    void likeEventSucceeded() {
        assertTrue(user.getLikeByMe().isEmpty());

        user.likeEvent(event1);

        assertEquals(1, user.getLikeByMe().size());
        assertTrue(user.getLikeByMe().contains(event1));

        user.likeEvent(event2);

        assertEquals(2, user.getLikeByMe().size());
        assertTrue(user.getLikeByMe().contains(event2));
    }

    @Test
    void unlikeEventSucceeded() {
        user.likeEvent(event1);
        user.likeEvent(event2);
        assertEquals(2, user.getLikeByMe().size());

        user.unlikeEvent(event1);

        assertEquals(1, user.getLikeByMe().size());
        assertFalse(user.getLikeByMe().contains(event1));
        assertTrue(user.getLikeByMe().contains(event2));

        user.unlikeEvent(event2);

        assertTrue(user.getLikeByMe().isEmpty());
    }
}