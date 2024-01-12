package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.Ticket;
import cz.cvut.iarylser.dao.entity.TicketStatus;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.repository.TicketRepository;
import cz.cvut.iarylser.dao.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TicketService ticketService;

    Ticket ticket1, ticket2;

    @BeforeEach
    void setUp() {
        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
        initTickets();
    }

    void initTickets(){
        ticket1 = new Ticket();
        ticket1.setId(1L);
        ticket1.setDetails("Details for ticket1");
        ticket1.setEventId(2L);
        ticket1.setIdCustomer(3L);
        ticket1.setIdOrganizer(4L);
        ticket1.setTicketStatus(TicketStatus.ACTIVE);

        ticket2 = new Ticket();
        ticket2.setId(5L);
        ticket2.setDetails("Details for ticket2");
        ticket2.setEventId(6L);
        ticket2.setIdCustomer(7L);
        ticket2.setIdOrganizer(8L);
        ticket2.setTicketStatus(TicketStatus.ACTIVE);
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
        Long customerId = 3L;
        Mockito.when(ticketRepository.findByIdCustomer(customerId)).thenReturn(Arrays.asList(ticket1));

        List<Ticket> results = ticketService.getTicketByUser(customerId);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(ticket1.getId(), results.get(0).getId());
        assertEquals(ticket1.getDetails(), results.get(0).getDetails());
        assertEquals(ticket1.getIdCustomer(), results.get(0).getIdCustomer());
        assertEquals(ticket1.getIdOrganizer(), results.get(0).getIdOrganizer());
        assertEquals(ticket1.getEventId(), results.get(0).getEventId());

        Mockito.verify(ticketRepository).findByIdCustomer(customerId);
    }

    @Test
    void createTicket() {
        Event event = new Event();
        event.setId(1L);
        event.setIdOrganizer(2L);
        event.setTickets(new ArrayList<>());
        event.setDateAndTime(LocalDateTime.now());

        User customer = new User();
        customer.setId(3L);
        customer.setTickets(new ArrayList<>());

        Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        Ticket result = ticketService.createTicket(event, customer);

        assertNotNull(result);
        assertEquals(event.getId(), result.getEventId());
        assertEquals(customer.getId(), result.getIdCustomer());
        assertEquals(event.getIdOrganizer(), result.getIdOrganizer());
        assertEquals(TicketStatus.ACTIVE, result.getTicketStatus());
        assertEquals(customer, result.getUser());
        assertEquals(event, result.getEvent());
        assertTrue(customer.getTickets().contains(result));
        assertTrue(event.getTickets().contains(result));

        Mockito.verify(ticketRepository).save(result);
        Mockito.verify(userRepository).save(customer);
    }

    @Test
    void convertToDto() {

        TicketDTO ticketDTO = ticketService.convertToDto(ticket1);

        assertNotNull(ticketDTO);
        assertEquals(ticket1.getId(), ticketDTO.getId());
        assertEquals(ticket1.getEventId(), ticketDTO.getEventId());
        assertEquals(ticket1.getIdCustomer(), ticketDTO.getIdCustomer());
        assertEquals(ticket1.getIdOrganizer(),ticketDTO.getIdOrganizer());
        assertEquals(ticket1.getDetails(),ticketDTO.getDetails());
        assertEquals(ticket1.getTicketStatus(),ticketDTO.getTicketStatus());
    }

    @Test
    void convertTicketsToDTOs() {

        List<Ticket> tickets = Arrays.asList(ticket1, ticket2);

        List<TicketDTO> ticketDTOs = ticketService.convertTicketsToDTOs(tickets);

        assertNotNull(ticketDTOs);
        assertEquals(2, ticketDTOs.size());

        TicketDTO dto1 = ticketDTOs.get(0);
        assertEquals(ticket1.getId(), dto1.getId());
        assertEquals(ticket1.getEventId(), dto1.getEventId());
        assertEquals(ticket1.getIdCustomer(), dto1.getIdCustomer());
        assertEquals(ticket1.getIdOrganizer(), dto1.getIdOrganizer());
        assertEquals(ticket1.getDetails(), dto1.getDetails());
        assertEquals(ticket1.getTicketStatus(), dto1.getTicketStatus());

        TicketDTO dto2 = ticketDTOs.get(1);
        assertEquals(ticket2.getId(), dto2.getId());
        assertEquals(ticket2.getEventId(), dto2.getEventId());
        assertEquals(ticket2.getIdCustomer(), dto2.getIdCustomer());
        assertEquals(ticket2.getIdOrganizer(), dto2.getIdOrganizer());
        assertEquals(ticket2.getDetails(), dto2.getDetails());
        assertEquals(ticket2.getTicketStatus(), dto2.getTicketStatus());
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