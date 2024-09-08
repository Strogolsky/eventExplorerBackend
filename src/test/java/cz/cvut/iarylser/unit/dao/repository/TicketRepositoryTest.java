package cz.cvut.iarylser.unit.dao.repository;

import cz.cvut.iarylser.dao.entity.Ticket;
import cz.cvut.iarylser.dao.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class TicketRepositoryTest {
    @Autowired
    TicketRepository ticketRepository;
    Ticket ticket;
    @BeforeEach
    void setUp() {
        ticket = new Ticket();
        ticket.setEventId(1L);
        ticket.setCustomerId(2L);
        ticket.setOrganizerId(3L);
        ticket.setId(4L);
    }

    @Test
    void findByIdCustomerSucceeded() {
        // give
        ticketRepository.save(ticket);
        Long idCustomer = 2L;
        // when
        List<Ticket> found = ticketRepository.findByCustomerId(idCustomer);
        // then
        assertFalse(found.isEmpty());
        for(Ticket ticket: found){
            assertEquals(ticket.getCustomerId(), idCustomer);
        }

    }
    @Test
    void findByIdCustomerFailure() {
        // give
        ticketRepository.save(ticket);
        Long idCustomer = 4L;
        // when
        List<Ticket> found = ticketRepository.findByCustomerId(idCustomer);
        // then
        assertTrue(found.isEmpty());
    }
}