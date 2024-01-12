package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.entity.Ticket;
import cz.cvut.iarylser.dao.repository.TicketRepository;
import cz.cvut.iarylser.dao.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceTest {
    @Mock
    private TicketService ticketService;
    @InjectMocks
    private TicketRepository ticketRepository;
    @InjectMocks
    private UserRepository userRepository;

    Ticket ticket1, ticket2;

    @BeforeEach
    void setUp() {
        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
        initTickets();
    }

    void initTickets(){
        ticket1.setId(1L);
        ticket1.setDetails("Details for ticket1");
        ticket1.setEventId(2L);
        ticket1.setIdCustomer(3L);
        ticket1.setIdOrganizer(4L);


        ticket2.setId(5L);
        ticket2.setDetails("Details for ticket2");
        ticket1.setEventId(6L);
        ticket1.setIdCustomer(7L);
        ticket1.setIdOrganizer(8L);
    }

    @Test
    void getTicketById() {
        Long ticketId = 1L;
        Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket1));

        Ticket result = ticketService.getTicketById(ticketId);

        assertNotNull(result);
        assertEquals(ticket1.getId(), result.getId());
        assertEquals(ticket1.getDetails(), result.getDetails());
        assertEquals(ticket1.getIdCustomer(), result.getIdCustomer());
        assertEquals(ticket1.getIdOrganizer(), result.getIdOrganizer());
        assertEquals(ticket1.getEventId(), result.getEventId());


        Mockito.verify(ticketRepository).findById(ticketId);
    }

    @Test
    void getTicketByUser() {
    }

    @Test
    void createTicket() {
    }

    @Test
    void convertToDto() {
    }

    @Test
    void convertTicketsToDTOs() {
    }

    @Test
    void updateTicket() {
    }

    @Test
    void setDetailsFromEvent() {
    }

    @Test
    void deleteTicket() {
    }
}