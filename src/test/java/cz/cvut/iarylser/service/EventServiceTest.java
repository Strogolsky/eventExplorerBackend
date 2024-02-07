package cz.cvut.iarylser.service;

import cz.cvut.iarylser.controller.EventController;
import cz.cvut.iarylser.dao.DTO.EventDTO;
import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.DTO.PurchaseRequest;
import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.Ticket;
import cz.cvut.iarylser.dao.entity.Topics;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.repository.EventRepository;
import cz.cvut.iarylser.dao.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
@SpringBootTest
class EventServiceTest {

    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TicketService ticketService;

    @Autowired
    private EventService eventService;

    Event event1, event2;
    @BeforeEach
    void setUp() {
        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
        initEvents();
    }
    void initEvents(){
        LocalDateTime eventDateTime = LocalDateTime.of(2024, 1, 15, 20, 0);

        event1 = new Event();
        event1.setId(1L);
        event1.setIdOrganizer(10L);
        event1.setTitle("Concert 1");
        event1.setOrganizer("Organizer 1");
        event1.setDateAndTime(eventDateTime);
        event1.setTicketPrice(500);
        event1.setLocation("Moscow");
        event1.setCapacity(100);
        event1.setSoldTickets(50);
        event1.setDescription("Description of event 1");
        event1.setTopic(Topics.CULTURE);
        event1.setAgeRestriction(false);

        event2 = new Event();
        event2.setId(2L);
        event2.setIdOrganizer(20L);
        event2.setTitle("Concert 2");
        event2.setOrganizer("Organizer 2");
        event2.setDateAndTime(eventDateTime.plusDays(1));
        event2.setTicketPrice(600);
        event2.setLocation("Saint Petersburg");
        event2.setCapacity(200);
        event2.setSoldTickets(100);
        event2.setDescription("Description of event 2");
        event2.setTopic(Topics.EDUCATION);
        event2.setAgeRestriction(true);

    }
    public static void assertEventsEqual(Event expected, Event actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getIdOrganizer(), actual.getIdOrganizer());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getOrganizer(), actual.getOrganizer());
        assertEquals(expected.getDateAndTime(), actual.getDateAndTime());
        assertEquals(expected.getTicketPrice(), actual.getTicketPrice());
        assertEquals(expected.getLocation(), actual.getLocation());
        assertEquals(expected.getCapacity(), actual.getCapacity());
        assertEquals(expected.getSoldTickets(), actual.getSoldTickets());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getTopic(), actual.getTopic());
        assertEquals(expected.isAgeRestriction(), actual.isAgeRestriction());
    }

    private void assertEventAndDtoEqual(Event event, EventDTO eventDTO) {
        assertEquals(event.getId(), eventDTO.getId());
        assertEquals(event.getTitle(), eventDTO.getTitle());
        assertEquals(event.getDateAndTime(), eventDTO.getDateAndTime());
        assertEquals(event.getTicketPrice(), eventDTO.getTicketPrice());
        assertEquals(event.getLocation(), eventDTO.getLocation());
        assertEquals(event.getCapacity(), eventDTO.getCapacity());
        assertEquals(event.getOrganizer(), eventDTO.getOrganizer());
        assertEquals(event.getSoldTickets(), eventDTO.getSoldTickets());
        assertEquals(event.getDescription(), eventDTO.getDescription());
        assertEquals(event.getTopic(), eventDTO.getTopic());
        assertEquals(event.isAgeRestriction(), eventDTO.isAgeRestriction());
    }


    @Test
    void getAllEvents() {
        List<Event> events = Arrays.asList(event1, event2);
        when(eventRepository.findAll()).thenReturn(events);

        List<Event> result = eventService.getAllEvents();

        assertNotNull(result);
        assertFalse(result.isEmpty());

        assertEquals(2, result.size());

        assertEventsEqual(event1,result.get(0));
        assertEventsEqual(event2,result.get(1));

        Mockito.verify(eventRepository).findAll();
    }

    @Test
    void getEventById() {
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event1));

        Event result = eventService.getEventById(eventId);

        assertNotNull(result);
        assertEventsEqual(event1,result);

        Mockito.verify(eventRepository).findById(eventId);
    }

    @Test
    void createEventSucceeded() {
        Event newEvent = new Event();
        newEvent.setTitle(event1.getTitle());
        newEvent.setDateAndTime(event1.getDateAndTime());
        newEvent.setTicketPrice(event1.getTicketPrice());
        newEvent.setLocation(event1.getLocation());
        newEvent.setCapacity(event1.getCapacity());
        newEvent.setSoldTickets(event1.getSoldTickets());
        newEvent.setDescription(event1.getDescription());
        newEvent.setTopic(event1.getTopic());
        newEvent.setAgeRestriction(event1.isAgeRestriction());
        newEvent.setOrganizer("OrganizerName");

        User organizer = new User();
        organizer.setId(10L);
        organizer.setNickname("OrganizerName");

        when(userRepository.findByNickname("OrganizerName")).thenReturn(organizer);
        when(eventRepository.save(Mockito.any(Event.class))).thenAnswer(i -> i.getArguments()[0]);

        Event createdEvent = eventService.createEvent(newEvent);

        assertEventsEqual(newEvent, createdEvent);
        assertTrue(organizer.getCreatedEvents().contains(createdEvent));

        Mockito.verify(userRepository).findByNickname("OrganizerName");
        Mockito.verify(userRepository).save(organizer);
        Mockito.verify(eventRepository).save(newEvent);
    }

    @Test
    void createEventFailure() {
        Event newEvent = new Event();
        newEvent.setOrganizer("nonexistent_organizer");
        when(userRepository.findByNickname(anyString())).thenReturn(null);

        Event result = eventService.createEvent(newEvent);
        assertNull(result);
    }

    @Test
    void purchaseTicketSucceeded() {
        Long eventId = 1L;
        String customerNickname = "customerNick";
        int quantity = 2;
        int age = 25;

        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        Ticket ticket3 = new Ticket();

        Event event = new Event();
        event.setId(eventId);
        event.setCapacity(10);
        event.setSoldTickets(3);
        event.setAgeRestriction(false);
        event.setDateAndTime(LocalDateTime.now());
        event.getTickets().add(ticket1);
        event.getTickets().add(ticket2);
        event.getTickets().add(ticket3);

        User customer = new User();
        customer.setNickname(customerNickname);
        customer.setAge(age);

        PurchaseRequest request = new PurchaseRequest();
        request.setCustomer(customerNickname);
        request.setQuantity(quantity);

        Ticket ticket4 = new Ticket();
        Ticket ticket5 = new Ticket();
        List<Ticket> tickets = List.of(ticket1, ticket2);
        List<TicketDTO> ticketDTOs = List.of(new TicketDTO(), new TicketDTO());

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findByNickname(customerNickname)).thenReturn(customer);
        when(ticketService.createTicket(Mockito.any(), Mockito.any())).thenReturn(ticket1, ticket2);
        when(ticketService.convertTicketsToDTOs(tickets)).thenReturn(ticketDTOs);

        List<TicketDTO> result = eventService.purchaseTicket(eventId,request);


        assertNotNull(result);
        assertEquals(2, result.size());
        Mockito.verify(eventRepository).findById(eventId);
        Mockito.verify(userRepository).findByNickname(customerNickname);
        Mockito.verify(ticketService, Mockito.times(quantity)).createTicket(Mockito.any(Event.class), Mockito.any(User.class));
        Mockito.verify(eventRepository).save(event);
        Mockito.verify(userRepository).save(customer);
        assertEquals(5, event.getSoldTickets());
    }

    @Test
    void purchaseTicketFailure() {
        Long eventId = 1L;
        PurchaseRequest request = new PurchaseRequest();
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        List<TicketDTO> result = eventService.purchaseTicket(eventId, request);

        assertNull(result);
    }

    @Test
    void updateEventFailureNotFound() {
        Long eventId = 1L;
        Event updatedEvent = new Event();
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        Event result = eventService.updateEvent(eventId, updatedEvent);

        assertNull(result);
    }

    @Test
    void updateEventFailureCapacity() {
        Long eventId = 1L;
        Event existingEvent = new Event();
        existingEvent.setCapacity(10);
        existingEvent.setSoldTickets(11);
        Event updatedEvent = new Event();
        updatedEvent.setCapacity(5);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));

        Event result = eventService.updateEvent(eventId, updatedEvent);

        assertNull(result);
    }



    @Test
    void updateEventSucceeded() {
        Long eventId = 1L;
        Event existingEvent = new Event();
        existingEvent.setId(eventId);
        existingEvent.setTitle("Original Title");
        existingEvent.setDateAndTime(LocalDateTime.now());
        existingEvent.setDescription("Original Description");
        existingEvent.setLocation("Original Location");
        existingEvent.setTicketPrice(100);
        existingEvent.setCapacity(500);
        existingEvent.setSoldTickets(200);
        existingEvent.setAgeRestriction(false);

        Event updatedEventData = new Event();
        updatedEventData.setId(eventId);
        updatedEventData.setTitle("Updated Title");
        updatedEventData.setDateAndTime(LocalDateTime.now().plusDays(1));
        updatedEventData.setDescription("Updated Description");
        updatedEventData.setLocation("Updated Location");
        updatedEventData.setTicketPrice(150);
        updatedEventData.setCapacity(600);
        updatedEventData.setSoldTickets(200);
        updatedEventData.setAgeRestriction(true);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(Mockito.any(Event.class))).thenAnswer(i -> i.getArguments()[0]);

        Event updatedEvent = eventService.updateEvent(eventId, updatedEventData);

        assertNotNull(updatedEvent);
        assertEventsEqual(existingEvent,updatedEvent);

        Mockito.verify(eventRepository).findById(eventId);
        Mockito.verify(eventRepository).save(Mockito.any(Event.class));

    }

    @Test
    void updateForOrgChange() {
        Event event = new Event();
        event.setId(1L);
        event.setOrganizer("Original Organizer");

        User newOrganizer = new User();
        newOrganizer.setNickname("New Organizer");

        when(eventRepository.save(Mockito.any(Event.class))).thenAnswer(i -> i.getArguments()[0]);

        eventService.updateForOrgChange(event, newOrganizer);

        assertEquals(newOrganizer.getNickname(), event.getOrganizer());
        Mockito.verify(eventRepository).save(event);
    }

    @Test
    void deleteEventSucceeded() {
        Long eventId = 1L;
        Long organizerId = 2L;

        User organizer = new User();
        organizer.setId(organizerId);

        Event eventToDelete = new Event();
        eventToDelete.setId(eventId);
        eventToDelete.setIdOrganizer(organizerId);
        eventToDelete.setUser(organizer);

        organizer.getCreatedEvents().add(eventToDelete);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventToDelete));
        when(userRepository.findById(organizerId)).thenReturn(Optional.of(organizer));
        Mockito.doNothing().when(eventRepository).deleteById(eventId);

        boolean result = eventService.deleteEvent(eventId);

        assertTrue(result);
        assertFalse(organizer.getCreatedEvents().contains(eventToDelete));
        Mockito.verify(eventRepository).deleteById(eventId);
        Mockito.verify(eventRepository).findById(eventId);
        Mockito.verify(userRepository).findById(organizerId);
        Mockito.verify(userRepository).save(organizer);
    }

    @Test
    void deleteEventFailureEventNotFound() {
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        boolean result = eventService.deleteEvent(eventId);

        assertFalse(result);
    }

    @Test
    void deleteEventFailureOrganizerNotFound() {
        Long eventId = 1L;
        Event event = new Event();
        event.setIdOrganizer(1L);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findById(event.getIdOrganizer())).thenReturn(Optional.empty());

        boolean result = eventService.deleteEvent(eventId);

        assertFalse(result);
    }

    @Test
    void convertToDto() {

        EventDTO eventDTO = eventService.convertToDto(event1);

        assertEventAndDtoEqual(event1, eventDTO);
    }

    @Test
    void convertToDTOList() {
        List<Event> events = Arrays.asList(event1,event2);

        List<EventDTO> eventDTOs = eventService.convertToDTOList(events);

        assertNotNull(eventDTOs);
        assertEquals(events.size(), eventDTOs.size());
        for (int i = 0; i < events.size(); i++) {
            assertEventAndDtoEqual(events.get(i), eventDTOs.get(i));
        }
    }

    @Test
    void likeEventSucceeded() {
        User user = new User();
        user.setId(1L);
        user.setLikeByMe(new HashSet<>());

        Event event = new Event();
        event.setId(2L);
        event.setLikeBy(new ArrayList<>());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(eventRepository.save(Mockito.any(Event.class))).thenAnswer(i -> i.getArguments()[0]);

        boolean result = eventService.likeEvent(event.getId(), user.getId());

        assertTrue(result);
        assertTrue(user.getLikeByMe().stream().anyMatch(e -> e.getId().equals(event.getId())));
        assertTrue(event.getLikeBy().stream().anyMatch(u -> u.getId().equals(user.getId())));
        Mockito.verify(userRepository).save(user);
        Mockito.verify(eventRepository).save(event);
    }

    @Test
    void unlikeEventSucceeded() {
        User user = new User();
        user.setId(1L);
        user.setLikeByMe(new HashSet<>());

        Event event = new Event();
        event.setId(2L);
        event.setLikeBy(new ArrayList<>());

        user.getLikeByMe().add(event);
        event.getLikeBy().add(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(eventRepository.save(Mockito.any(Event.class))).thenAnswer(i -> i.getArguments()[0]);

        boolean result = eventService.unlikeEvent(event.getId(), user.getId());

        assertTrue(result);
        assertTrue(user.getLikeByMe().isEmpty());
        assertTrue(event.getLikeBy().isEmpty());
        Mockito.verify(userRepository).save(user);
        Mockito.verify(eventRepository).save(event);
    }

    @Test
    void likeEventFailure() {
        Long eventId = 1L;
        Long userId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        boolean result = eventService.likeEvent(eventId, userId);

        assertFalse(result);
    }

    @Test
    void unlikeEventFailure() {
        Long eventId = 1L;
        Long userId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        boolean result = eventService.unlikeEvent(eventId, userId);

        assertFalse(result);
    }
    @Test
    void getByLikedGreaterThanSuccessful() {
        int likes = 10;
        Event event1 = new Event();
        Event event2 = new Event();
        List<Event> events = Arrays.asList(event1, event2);

        when(eventRepository.findByLikedGreaterThan(likes)).thenReturn(events);

        List<Event> result = eventService.getByLikedGreaterThan(likes);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(events));
    }
    @Test
    void getByLikedGreaterThanFailure() {
        int likes = 10;
        List<Event> events = new ArrayList<>();

        when(eventRepository.findByLikedGreaterThan(likes)).thenReturn(events);

        List<Event> result = eventService.getByLikedGreaterThan(likes);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}