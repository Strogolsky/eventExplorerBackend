package cz.cvut.iarylser.Unit.dao.entity;

import cz.cvut.iarylser.dao.entity.Event;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {



    @Test
    void getAvailableSeat() {
        Event event = new Event();
        event.setSoldTickets(15);
        event.setCapacity(20);

        assertEquals(event.getAvailableSeat(), 5);
    }

    @Test
    void updateCapacity() {
        Event event = new Event();
        event.setCapacity(10);
        event.setSoldTickets(8);

        assertTrue(event.updateCapacity(9));
    }
}