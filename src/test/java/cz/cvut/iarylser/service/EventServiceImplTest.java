package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.dto.EventDTO;
import cz.cvut.iarylser.dao.dto.TicketResponse;
import cz.cvut.iarylser.dao.dto.PurchaseRequest;
import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.Ticket;
import cz.cvut.iarylser.dao.entity.Topics;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.mappersDto.TicketMapperDTO;
import cz.cvut.iarylser.dao.repository.EventRepository;
import cz.cvut.iarylser.dao.repository.UserRepository;
import cz.cvut.iarylser.service.EventServiceImpl;
import cz.cvut.iarylser.service.TicketServiceImpl;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
@SpringBootTest
@ContextConfiguration(classes = EventServiceImpl.class)
class EventServiceImplTest {

    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TicketServiceImpl ticketService;

    @MockBean
    private TicketMapperDTO ticketMapperDTO;

    @Autowired
    private EventServiceImpl eventService;

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
        event1.setOrganizerId(10L);
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
        event2.setOrganizerId(20L);
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
        assertEquals(expected.getOrganizerId(), actual.getOrganizerId());
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

        List<Event> result = eventService.getAll();

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

        Event result = eventService.getById(eventId);

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
        organizer.setUsername("OrganizerName");

        when(userRepository.findByUsername("OrganizerName")).thenReturn(organizer);
        when(eventRepository.save(Mockito.any(Event.class))).thenAnswer(i -> i.getArguments()[0]);

        Event createdEvent = eventService.create(newEvent);

        assertEventsEqual(newEvent, createdEvent);
        assertTrue(organizer.getCreatedEvents().contains(createdEvent));

        Mockito.verify(userRepository).findByUsername("OrganizerName");
        Mockito.verify(userRepository).save(organizer);
        Mockito.verify(eventRepository).save(newEvent);
    }

    @Test
    void createEventFailure() {
        Event newEvent = new Event();
        newEvent.setOrganizer("nonexistent_organizer");
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        Event result = eventService.create(newEvent);
        assertNull(result);
    }

    @Test
    public void purchaseTicketSuccessTest() throws EntityNotFoundException, IllegalStateException {
        // Arrange
        Event event = new Event();
        event.setId(1L);
        event.setCapacity(10);
        event.setTicketPrice(50);
        event.setSoldTickets(0);
        event.setAgeRestriction(false);

        User customer = new User();
        customer.setUsername("testUser");
        customer.setAge(20);
        customer.setBalance(100);

        User author = new User();
        author.setUsername("testAuthor");
        author.setBalance(50);


        event.setOrganizer("testAuthor");

        PurchaseRequest request = new PurchaseRequest();
        request.setCustomer("testUser");
        request.setQuantity(2);

        // Mocking the repository and service methods
        Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(customer);
        Mockito.when(userRepository.findByUsername(event.getOrganizer())).thenReturn(author);
        Mockito.when(ticketService.create(event, customer)).thenReturn(new Ticket());
        Mockito.when(ticketMapperDTO.toDTOList(Mockito.anyList())).thenReturn(new ArrayList<>());

        // Act
        List<TicketResponse> result = eventService.purchaseTicket(1L, request);

        // Assert
        assertNotNull(result);

        // Check balances
        assertEquals(0, customer.getBalance());
        assertEquals(150, author.getBalance());

        // Verify interactions
        Mockito.verify(eventRepository).save(event);
        Mockito.verify(userRepository).save(customer);
        Mockito.verify(userRepository).save(author);
        Mockito.verify(ticketService, Mockito.times(2)).create(event, customer);
    }

    @Test
    public void PurchaseTicketEventNotFoundTest() throws EntityNotFoundException {
        Long eventId = 1L;
        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        PurchaseRequest request = new PurchaseRequest();
        request.setCustomer("testUser");
        request.setQuantity(2);

        assertThrows(EntityNotFoundException.class, () -> eventService.purchaseTicket(eventId, request));
    }
    @Test
    public void PurchaseTicketUserNotFoundTest() throws EntityNotFoundException {
        Event event = new Event();
        event.setId(1L);

        Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(null);

        PurchaseRequest request = new PurchaseRequest();
        request.setCustomer("testUser");
        request.setQuantity(2);

        assertThrows(EntityNotFoundException.class, () -> eventService.purchaseTicket(1L, request));
    }
    @Test
    public void PurchaseTicketNotEnoughSeatsTest() throws EntityNotFoundException {
        Event event = new Event();
        event.setId(1L);
        event.setCapacity(1);
        event.setSoldTickets(0);

        User customer = new User();
        customer.setUsername("testUser");

        Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(customer);

        PurchaseRequest request = new PurchaseRequest();
        request.setCustomer("testUser");
        request.setQuantity(2);


        assertThrows(IllegalStateException.class, () -> eventService.purchaseTicket(1L, request));
    }
    @Test
    public void PurchaseTicketAgeRestrictionTest() throws EntityNotFoundException {
        Event event = new Event();
        event.setId(1L);
        event.setCapacity(5);
        event.setSoldTickets(0);
        event.setAgeRestriction(true);

        User customer = new User();
        customer.setUsername("testUser");
        customer.setAge(16);

        Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(customer);

        PurchaseRequest request = new PurchaseRequest();
        request.setCustomer("testUser");
        request.setQuantity(1);

        assertThrows(IllegalStateException.class, () -> eventService.purchaseTicket(1L, request));
    }
    @Test
    public void PurchaseTicketInsufficientBalanceTest() throws EntityNotFoundException {
        Event event = new Event();
        event.setId(1L);
        event.setCapacity(5);
        event.setSoldTickets(0);
        event.setTicketPrice(100);

        User customer = new User();
        customer.setUsername("testUser");
        customer.setBalance(50);

        Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(customer);

        PurchaseRequest request = new PurchaseRequest();
        request.setCustomer("testUser");
        request.setQuantity(1);

        assertThrows(IllegalStateException.class, () -> eventService.purchaseTicket(1L, request));
    }


    @Test
    void UpdateEventNotFoundTest() {
        Long eventId = 1L;
        Event updatedEvent = new Event();
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> eventService.update(eventId, updatedEvent));

    }

    @Test
    void updateEventFailureCapacityTest() {
        Long eventId = 1L;
        Event existingEvent = new Event();
        existingEvent.setCapacity(10);
        existingEvent.setSoldTickets(11);
        Event updatedEvent = new Event();
        updatedEvent.setCapacity(5);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));

        assertThrows(IllegalStateException.class, () -> eventService.update(eventId, updatedEvent));
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

        Event updatedEvent = eventService.update(eventId, updatedEventData);

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
        newOrganizer.setUsername("New Organizer");

        when(eventRepository.save(Mockito.any(Event.class))).thenAnswer(i -> i.getArguments()[0]);

        eventService.updateForOrgChange(event, newOrganizer);

        assertEquals(newOrganizer.getUsername(), event.getOrganizer());
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
        eventToDelete.setOrganizerId(organizerId);
        eventToDelete.setUser(organizer);

        organizer.getCreatedEvents().add(eventToDelete);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventToDelete));
        when(userRepository.findById(organizerId)).thenReturn(Optional.of(organizer));
        Mockito.doNothing().when(eventRepository).deleteById(eventId);

        boolean result = eventService.delete(eventId);

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

        boolean result = eventService.delete(eventId);

        assertFalse(result);
    }

    @Test
    void deleteEventFailureOrganizerNotFound() {
        Long eventId = 1L;
        Event event = new Event();
        event.setOrganizerId(1L);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findById(event.getOrganizerId())).thenReturn(Optional.empty());

        boolean result = eventService.delete(eventId);

        assertFalse(result);
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

        boolean result = eventService.like(event.getId(), user.getId());

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

        boolean result = eventService.unlike(event.getId(), user.getId());

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

        boolean result = eventService.like(eventId, userId);

        assertFalse(result);
    }

    @Test
    void unlikeEventFailure() {
        Long eventId = 1L;
        Long userId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        boolean result = eventService.unlike(eventId, userId);

        assertFalse(result);
    }

}