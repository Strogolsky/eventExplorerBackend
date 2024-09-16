package cz.cvut.iarylser.unit.dao.repository;

import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
class EventRepositoryTest {

    @Test
    void findByIdOrganizer() {
    }
    @Autowired
    private EventRepository eventRepository;

    @Test
    void testFindByIdOrganizer() {
        Long organizerId1 = 1L;
        Long organizerId2 = 2L;

        Event event1 = new Event();
        event1.setOrganizerId(organizerId1);
        event1.setTitle("Event1");
        eventRepository.save(event1);

        Event event2 = new Event();
        event2.setOrganizerId(organizerId1);
        event2.setTitle("Event2");
        eventRepository.save(event2);

        Event event3 = new Event();
        event3.setOrganizerId(organizerId2);
        event3.setTitle("Event3");
        eventRepository.save(event3);

        List<Event> eventsOfOrganizer1 = eventRepository.findByOrganizerId(organizerId1);

        assertThat(eventsOfOrganizer1).contains(event1, event2);
        assertThat(eventsOfOrganizer1).doesNotContain(event3);
        assertThat(eventsOfOrganizer1).hasSize(2);
    }
}