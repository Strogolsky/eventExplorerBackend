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
        assertTicketAndDtoEquality(ticket1,ticketDTO);
    }

    @Test
    void convertTicketsToDTOs() {

        List<Ticket> tickets = Arrays.asList(ticket1, ticket2);

        List<TicketDTO> ticketDTOs = ticketService.convertTicketsToDTOs(tickets);

        assertNotNull(ticketDTOs);
        assertEquals(2, ticketDTOs.size());

        TicketDTO dto1 = ticketDTOs.get(0);
        assertTicketAndDtoEquality(ticket1,dto1);

        TicketDTO dto2 = ticketDTOs.get(1);
        assertTicketAndDtoEquality(ticket2,dto2);
    }

    @Test
    void updateTicket() {
        Ticket updatedTicket = new Ticket();
        updatedTicket.setId(1L);
        updatedTicket.setEventId(2L);
        updatedTicket.setIdCustomer(3L);
        updatedTicket.setIdOrganizer(4L);
        updatedTicket.setDetails("Updated Details");
        updatedTicket.setTicketStatus(TicketStatus.INVALID);

        Mockito.when(ticketRepository.findById(ticket1.getId())).thenReturn(Optional.of(ticket1));
        Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        Ticket result = ticketService.updateTicket(ticket1.getId(), updatedTicket);

        assertNotNull(result);
        assertTicketsEquality(updatedTicket,result);

        Mockito.verify(ticketRepository).findById(ticket1.getId());
        Mockito.verify(ticketRepository).save(result);
    }

    @Test
    void setDetailsFromEvent() {

        Event event = new Event();
        event.setTitle("Concert");
        event.setDateAndTime(LocalDateTime.of(2024, 1, 15, 20, 0)); // Пример даты и времени
        event.setLocation("Main Arena");

        Ticket ticket = new Ticket();

        String expectedDetails = "Event: Concert, Date: 2024-01-15T20:00, Location: Main Arena";

        ticketService.setDetailsFromEvent(event, ticket);

        assertEquals(expectedDetails, ticket.getDetails());
    }

    @Test
    void deleteTicket() {
        Long ticketId = 1L;

        Mockito.when(ticketRepository.existsById(ticketId)).thenReturn(true);
        Mockito.doNothing().when(ticketRepository).deleteById(ticketId);

        boolean result = ticketService.deleteTicket(ticketId);

        assertTrue(result);
        Mockito.verify(ticketRepository).existsById(ticketId);
        Mockito.verify(ticketRepository).deleteById(ticketId);
    }
    void assertTicketAndDtoEquality(Ticket ticket, TicketDTO ticketDTO) {
        assertEquals(ticket.getId(), ticketDTO.getId());
        assertEquals(ticket.getEventId(), ticketDTO.getEventId());
        assertEquals(ticket.getIdCustomer(), ticketDTO.getIdCustomer());
        assertEquals(ticket.getIdOrganizer(), ticketDTO.getIdOrganizer());
        assertEquals(ticket.getDetails(), ticketDTO.getDetails());
        assertEquals(ticket.getTicketStatus(), ticketDTO.getTicketStatus());
    }
    void assertTicketsEquality(Ticket expectedTicket, Ticket actualTicket) {
        assertEquals(expectedTicket.getId(), actualTicket.getId());
        assertEquals(expectedTicket.getEventId(), actualTicket.getEventId());
        assertEquals(expectedTicket.getIdCustomer(), actualTicket.getIdCustomer());
        assertEquals(expectedTicket.getIdOrganizer(), actualTicket.getIdOrganizer());
        assertEquals(expectedTicket.getDetails(), actualTicket.getDetails());
//        assertEquals(expectedTicket.getTicketStatus(), actualTicket.getTicketStatus());
    }
}