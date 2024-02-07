package cz.cvut.iarylser.Unit.dao.repository;

import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.repository.EventRepository;
import cz.cvut.iarylser.dao.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class EventRepositoryTest {

    @Test
    void findByIdOrganizer() {
    }
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void FindByLikedGreaterThanExist() {
        User user1 = new User();
        user1.setNickname("User1");
        userRepository.save(user1);

        User user2 = new User();
        user2.setNickname("User2");
        userRepository.save(user2);

        Event event1 = new Event();
        event1.setTitle("Event1");
        eventRepository.save(event1);

        event1.getLikeBy().add(user1);
        event1.getLikeBy().add(user2);

        eventRepository.save(event1);

        int likesThreshold = 1;
        Collection<Event> results = eventRepository.findByLikedGreaterThan(likesThreshold);

        assertThat(results).contains(event1);
        assertThat(results).hasSize(1);
    }
    @Test
    public void FindByLikedGreaterThanNotExist() {
        User user1 = new User();
        user1.setNickname("User1");
        userRepository.save(user1);

        User user2 = new User();
        user2.setNickname("User2");
        userRepository.save(user2);

        Event event1 = new Event();
        event1.setTitle("Event1");
        eventRepository.save(event1);

        event1.getLikeBy().add(user1);
        event1.getLikeBy().add(user2);

        eventRepository.save(event1);

        int likesThreshold = 2;
        Collection<Event> results = eventRepository.findByLikedGreaterThan(likesThreshold);

        assertThat(results).hasSize(0);
    }
    @Test
    public void testFindByIdOrganizer() {
        Long organizerId1 = 1L;
        Long organizerId2 = 2L;

        Event event1 = new Event();
        event1.setIdOrganizer(organizerId1);
        event1.setTitle("Event1");
        eventRepository.save(event1);

        Event event2 = new Event();
        event2.setIdOrganizer(organizerId1);
        event2.setTitle("Event2");
        eventRepository.save(event2);

        Event event3 = new Event();
        event3.setIdOrganizer(organizerId2);
        event3.setTitle("Event3");
        eventRepository.save(event3);

        List<Event> eventsOfOrganizer1 = eventRepository.findByIdOrganizer(organizerId1);

        assertThat(eventsOfOrganizer1).contains(event1, event2);
        assertThat(eventsOfOrganizer1).doesNotContain(event3);
        assertThat(eventsOfOrganizer1).hasSize(2);
    }
}