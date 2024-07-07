package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.repository.EventRepository;
import cz.cvut.iarylser.dao.repository.UserRepository;
import cz.cvut.iarylser.service.EventService;
import cz.cvut.iarylser.service.TicketService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class EventServiceImplTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketService ticketService;

    private User organizer;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAllEvents() {
        // Given
        Event event1 = new Event();
        event1.setTitle("Event 1");
        eventRepository.save(event1);

        Event event2 = new Event();
        event2.setTitle("Event 2");
        eventRepository.save(event2);

        // When
        List<Event> events = eventService.getAll();

        // Then
        Assertions.assertEquals(2, events.size());
    }
    @Test
    void getEventByIdSuccessful() {
        // Given
        Event event = new Event();
        event.setTitle("Successful Event");
        eventRepository.save(event);

        // When
        Event retrievedEvent = eventService.getById(event.getId());

        // Then
        Assertions.assertNotNull(retrievedEvent);
        Assertions.assertEquals(event.getId(), retrievedEvent.getId());
        Assertions.assertEquals(event.getTitle(), retrievedEvent.getTitle());
    }

    @Test
    void getEventByIdFailure() {
        // Given
        Long eventId = 999L;

        // When
        Event retrievedEvent = eventService.getById(eventId);

        // Then
        Assertions.assertNull(retrievedEvent);
    }
    @Test
    void likeEventSuccessful() {
        // Given
        Event event = new Event();
        event.setTitle("Liked Event");
        eventRepository.save(event);

        User user = new User();
        userRepository.save(user);

        // When
        boolean liked = eventService.like(event.getId(), user.getId());

        // Then
        Assertions.assertTrue(liked);
        Event updatedEvent = eventRepository.findById(event.getId()).orElse(null);
        Assertions.assertNotNull(updatedEvent);
        Assertions.assertTrue(updatedEvent.getLikeBy().contains(user));
    }

    @Test
    void likeEventFailure() {
        // Given
        Event event = new Event();
        event.setTitle("Liked Event");
        eventRepository.save(event);

        Long nonExistentUserId = 999L;

        // When
        boolean liked = eventService.like(event.getId(), nonExistentUserId);

        // Then
        Assertions.assertFalse(liked);
    }
}