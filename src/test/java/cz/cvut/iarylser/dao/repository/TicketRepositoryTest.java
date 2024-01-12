package cz.cvut.iarylser.dao.repository;

import cz.cvut.iarylser.dao.entity.Ticket;
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
        ticket.setIdCustomer(2L);
        ticket.setIdOrganizer(3L);
        ticket.setId(4L);
    }

    @Test
    void findByIdCustomerExistCustomer() {
        // give
        ticketRepository.save(ticket);
        Long idCustomer = 2L;
        // when
        List<Ticket> found = ticketRepository.findByIdCustomer(idCustomer);
        // then
        assertFalse(found.isEmpty());
        for(Ticket ticket: found){
            assertEquals(ticket.getIdCustomer(), idCustomer);
        }

    }
    @Test
    void findByIdCustomerNotExistCustomer() {
        // give
        ticketRepository.save(ticket);
        Long idCustomer = 4L;
        // when
        List<Ticket> found = ticketRepository.findByIdCustomer(idCustomer);
        // then
        assertTrue(found.isEmpty());
    }
}