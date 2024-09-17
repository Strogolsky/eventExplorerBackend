package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.dto.TicketResponse;
import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.Ticket;
import cz.cvut.iarylser.dao.entity.TicketStatus;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.mappersDto.TicketMapperDTO;
import cz.cvut.iarylser.dao.repository.TicketRepository;
import cz.cvut.iarylser.dao.repository.UserRepository;
import cz.cvut.iarylser.service.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@ContextConfiguration(classes = TicketServiceImpl.class)
class TicketServiceImplTest {

    @MockBean
    private TicketRepository ticketRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private TicketServiceImpl ticketServiceImpl;
    @MockBean
    private TicketMapperDTO ticketMapperDTO;

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
        ticket1.setCustomerId(3L);
        ticket1.setOrganizerId(4L);
        ticket1.setTicketStatus(TicketStatus.ACTIVE);

        ticket2 = new Ticket();
        ticket2.setId(5L);
        ticket2.setDetails("Details for ticket2");
        ticket2.setEventId(6L);
        ticket2.setCustomerId(7L);
        ticket2.setOrganizerId(8L);
        ticket2.setTicketStatus(TicketStatus.ACTIVE);
    }

    @Test
    void getTicketByIdSucceeded() {
        Long ticketId = 1L;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket1));

        Ticket result = ticketServiceImpl.getById(ticketId);

        assertNotNull(result);
        assertEquals(ticket1.getId(), result.getId());
        assertEquals(ticket1.getDetails(), result.getDetails());
        assertEquals(ticket1.getCustomerId(), result.getCustomerId());
        assertEquals(ticket1.getOrganizerId(), result.getOrganizerId());
        assertEquals(ticket1.getEventId(), result.getEventId());


        verify(ticketRepository).findById(ticketId);
    }

    @Test
    void getTicketByIdFailure() {
        Long ticketId = 1L;
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        Ticket result = ticketServiceImpl.getById(ticketId);

        assertNull(result);
    }

    @Test
    void getTicketByUserSucceeded() {
        Long customerId = 3L;
        when(ticketRepository.findByCustomerId(customerId)).thenReturn(Arrays.asList(ticket1));

        List<Ticket> results = ticketServiceImpl.getByUser(customerId);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(ticket1.getId(), results.get(0).getId());
        assertEquals(ticket1.getDetails(), results.get(0).getDetails());
        assertEquals(ticket1.getCustomerId(), results.get(0).getCustomerId());
        assertEquals(ticket1.getOrganizerId(), results.get(0).getOrganizerId());
        assertEquals(ticket1.getEventId(), results.get(0).getEventId());

        verify(ticketRepository).findByCustomerId(customerId);
    }

    @Test
    void getTicketByUserFailure() {
        Long userId = 1L;
        when(ticketRepository.findByCustomerId(userId)).thenReturn(Collections.emptyList());

        List<Ticket> result = ticketServiceImpl.getByUser(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void createTicket() {
        Event event = new Event();
        event.setId(1L);
        event.setOrganizerId(2L);
        event.setTickets(new ArrayList<>());
        event.setDateAndTime(LocalDateTime.now());

        User customer = new User();
        customer.setId(3L);
        customer.setTickets(new ArrayList<>());

        when(ticketRepository.save(Mockito.any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);
        when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        Ticket result = ticketServiceImpl.create(event, customer);

        assertNotNull(result);
        assertEquals(event.getId(), result.getEventId());
        assertEquals(customer.getId(), result.getCustomerId());
        assertEquals(event.getOrganizerId(), result.getOrganizerId());
        assertEquals(TicketStatus.ACTIVE, result.getTicketStatus());
        assertEquals(customer, result.getUser());
        assertEquals(event, result.getEvent());
        assertTrue(customer.getTickets().contains(result));
        assertTrue(event.getTickets().contains(result));

        verify(ticketRepository).save(result);
        verify(userRepository).save(customer);
    }

    @Test
    void updateTicketSucceeded() {
        Ticket updatedTicket = new Ticket();
        updatedTicket.setId(1L);
        updatedTicket.setEventId(2L);
        updatedTicket.setCustomerId(3L);
        updatedTicket.setOrganizerId(4L);
        updatedTicket.setDetails("Updated Details");
        updatedTicket.setTicketStatus(TicketStatus.INVALID);

        when(ticketRepository.findById(ticket1.getId())).thenReturn(Optional.of(ticket1));
        when(ticketRepository.save(Mockito.any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        Ticket result = ticketServiceImpl.update(ticket1.getId(), updatedTicket);

        assertNotNull(result);
        assertTicketsEquality(updatedTicket,result);

        verify(ticketRepository).findById(ticket1.getId());
        verify(ticketRepository).save(result);
    }

    @Test
    void updateTicketFailure() {
        Long ticketId = 1L;
        Ticket updatedTicket = new Ticket();
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        Ticket result = ticketServiceImpl.update(ticketId, updatedTicket);

        assertNull(result);
    }

    @Test
    void setDetailsFromEvent() {

        Event event = new Event();
        event.setTitle("Concert");
        event.setDateAndTime(LocalDateTime.of(2024, 1, 15, 20, 0)); // Пример даты и времени
        event.setLocation("Main Arena");

        Ticket ticket = new Ticket();

        String expectedDetails = "Event: Concert, Date: 2024-01-15T20:00, Location: Main Arena";

        ticketServiceImpl.setDetailsFromEvent(event, ticket);

        assertEquals(expectedDetails, ticket.getDetails());
    }

    @Test
    void deleteTicketSucceeded() {
        Long ticketId = 1L;

        when(ticketRepository.existsById(ticketId)).thenReturn(true);
        Mockito.doNothing().when(ticketRepository).deleteById(ticketId);

        boolean result = ticketServiceImpl.delete(ticketId);

        assertTrue(result);
        verify(ticketRepository).existsById(ticketId);
        verify(ticketRepository).deleteById(ticketId);
    }

    @Test
    void deleteTicketFailure() {
        Long ticketId = 1L;
        when(ticketRepository.existsById(ticketId)).thenReturn(false);

        boolean result = ticketServiceImpl.delete(ticketId);

        assertFalse(result);
        verify(ticketRepository, never()).deleteById(ticketId);
    }
    void assertTicketAndDtoEquality(Ticket ticket, TicketResponse ticketResponse) {
        assertEquals(ticket.getId(), ticketResponse.getId());
        assertEquals(ticket.getEventId(), ticketResponse.getEventId());
        assertEquals(ticket.getCustomerId(), ticketResponse.getCustomerId());
        assertEquals(ticket.getOrganizerId(), ticketResponse.getOrganizerId());
        assertEquals(ticket.getDetails(), ticketResponse.getDetails());
        assertEquals(ticket.getTicketStatus(), ticketResponse.getTicketStatus());
    }
    void assertTicketsEquality(Ticket expectedTicket, Ticket actualTicket) {
        assertEquals(expectedTicket.getId(), actualTicket.getId());
        assertEquals(expectedTicket.getEventId(), actualTicket.getEventId());
        assertEquals(expectedTicket.getCustomerId(), actualTicket.getCustomerId());
        assertEquals(expectedTicket.getOrganizerId(), actualTicket.getOrganizerId());
        assertEquals(expectedTicket.getDetails(), actualTicket.getDetails());
//        assertEquals(expectedTicket.getTicketStatus(), actualTicket.getTicketStatus());
    }
}