package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.DTO.EventDTO;
import cz.cvut.iarylser.dao.entity.Event;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TicketService ticketService;

    @InjectMocks
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
        Mockito.when(eventRepository.findAll()).thenReturn(events);

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
        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event1));

        Event result = eventService.getEventById(eventId);

        assertNotNull(result);
        assertEventsEqual(event1,result);

        Mockito.verify(eventRepository).findById(eventId);
    }

    @Test
    void createEvent() {
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
        newEvent.setOrganizer("OrganizerName"); // Example organizer name

        User organizer = new User();
        organizer.setId(10L);
        organizer.setNickname("OrganizerName");

        Mockito.when(userRepository.findByNickname("OrganizerName")).thenReturn(organizer);
        Mockito.when(eventRepository.save(Mockito.any(Event.class))).thenAnswer(i -> i.getArguments()[0]);

        Event createdEvent = eventService.createEvent(newEvent);

        assertEventsEqual(newEvent, createdEvent);

        Mockito.verify(userRepository).findByNickname("OrganizerName");
        Mockito.verify(userRepository).save(organizer);
        Mockito.verify(eventRepository).save(newEvent);
    }

    @Test
    void purchaseTicket() {
    }

    @Test
    void updateEvent() {
    }

    @Test
    void updateForOrgChange() {
    }

    @Test
    void deleteEvent() {
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
    void likeEvent() {
    }

    @Test
    void unlikeEvent() {
    }

    @Test
    void getRecommend() {
    }
}