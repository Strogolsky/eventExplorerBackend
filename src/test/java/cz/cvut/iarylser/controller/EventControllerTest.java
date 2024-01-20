package cz.cvut.iarylser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.iarylser.dao.DTO.EventDTO;
import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.Topics;
import cz.cvut.iarylser.dao.repository.EventRepository;
import cz.cvut.iarylser.dao.repository.UserRepository;
import cz.cvut.iarylser.service.EventService;
import cz.cvut.iarylser.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
    }

    @Test
    void getAllEventsSucceeded() throws Exception {
        EventDTO event1 = new EventDTO(1L, "Title 1", LocalDateTime.now(), 10, 100, "Location 1", 200, "Organizer 1", 50, "Description 1", Topics.EDUCATION, false);
        EventDTO event2 = new EventDTO(2L, "Title 2", LocalDateTime.now(), 20, 200, "Location 2", 300, "Organizer 2", 150, "Description 2", Topics.PARTY, true);
        List<EventDTO> allEvents = Arrays.asList(event1, event2);

        when(eventService.getAllEvents()).thenReturn(Arrays.asList(new Event(), new Event()));
        when(eventService.convertToDTOList(anyList())).thenReturn(allEvents);

        mockMvc.perform(get("/event")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(event1.getId()))
                .andExpect(jsonPath("$[0].title").value(event1.getTitle()))
                .andExpect(jsonPath("$[0].likes").value(event1.getLikes()))
                .andExpect(jsonPath("$[0].ticketPrice").value(event1.getTicketPrice()))
                .andExpect(jsonPath("$[0].location").value(event1.getLocation()))
                .andExpect(jsonPath("$[0].capacity").value(event1.getCapacity()))
                .andExpect(jsonPath("$[0].organizer").value(event1.getOrganizer()))
                .andExpect(jsonPath("$[0].soldTickets").value(event1.getSoldTickets()))
                .andExpect(jsonPath("$[0].description").value(event1.getDescription()))
                .andExpect(jsonPath("$[0].topic").value(event1.getTopic().toString()))
                .andExpect(jsonPath("$[0].ageRestriction").value(event1.isAgeRestriction()))

                .andExpect(jsonPath("$[1].id").value(event2.getId()))
                .andExpect(jsonPath("$[1].title").value(event2.getTitle()))
                .andExpect(jsonPath("$[1].likes").value(event2.getLikes()))
                .andExpect(jsonPath("$[1].ticketPrice").value(event2.getTicketPrice()))
                .andExpect(jsonPath("$[1].location").value(event2.getLocation()))
                .andExpect(jsonPath("$[1].capacity").value(event2.getCapacity()))
                .andExpect(jsonPath("$[1].organizer").value(event2.getOrganizer()))
                .andExpect(jsonPath("$[1].soldTickets").value(event2.getSoldTickets()))
                .andExpect(jsonPath("$[1].description").value(event2.getDescription()))
                .andExpect(jsonPath("$[1].topic").value(event2.getTopic().toString()))
                .andExpect(jsonPath("$[1].ageRestriction").value(event2.isAgeRestriction()));
}

    @Test
    void getEventByIdSucceeded() throws Exception {
        Long eventId = 1L;
        EventDTO eventDTO = new EventDTO(
                eventId,
                "Event Title",
                LocalDateTime.of(2023, 1, 20, 15, 30),
                150,
                20,
                "Event Location",
                300,
                "Event Organizer",
                75,
                "Event Description",
                Topics.CULTURE,
                false
        );

        when(eventService.getEventById(eventId)).thenReturn(new Event());
        when(eventService.convertToDto(any(Event.class))).thenReturn(eventDTO);

        mockMvc.perform(get("/event/{eventId}", eventId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventDTO.getId()))
                .andExpect(jsonPath("$.title").value(eventDTO.getTitle()))
                .andExpect(jsonPath("$.likes").value(eventDTO.getLikes()))
                .andExpect(jsonPath("$.ticketPrice").value(eventDTO.getTicketPrice()))
                .andExpect(jsonPath("$.location").value(eventDTO.getLocation()))
                .andExpect(jsonPath("$.capacity").value(eventDTO.getCapacity()))
                .andExpect(jsonPath("$.organizer").value(eventDTO.getOrganizer()))
                .andExpect(jsonPath("$.soldTickets").value(eventDTO.getSoldTickets()))
                .andExpect(jsonPath("$.description").value(eventDTO.getDescription()))
                .andExpect(jsonPath("$.topic").value(eventDTO.getTopic().toString()))
                .andExpect(jsonPath("$.ageRestriction").value(eventDTO.isAgeRestriction()));
    }

    @Test
    void createEventSucceeded() throws Exception {
        Event newEvent = new Event();
        EventDTO eventDTO = new EventDTO(
                1L,
                "Event Title",
                LocalDateTime.of(2023, 1, 20, 15, 30),
                150,
                20,
                "Event Location",
                300,
                "Event Organizer",
                75,
                "Event Description",
                Topics.EDUCATION,
                false
        );

        when(eventService.createEvent(any(Event.class))).thenReturn(newEvent);
        when(eventService.convertToDto(any(Event.class))).thenReturn(eventDTO);

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventDTO.getId()))
                .andExpect(jsonPath("$.title").value(eventDTO.getTitle()))
                .andExpect(jsonPath("$.likes").value(eventDTO.getLikes()))
                .andExpect(jsonPath("$.ticketPrice").value(eventDTO.getTicketPrice()))
                .andExpect(jsonPath("$.location").value(eventDTO.getLocation()))
                .andExpect(jsonPath("$.capacity").value(eventDTO.getCapacity()))
                .andExpect(jsonPath("$.organizer").value(eventDTO.getOrganizer()))
                .andExpect(jsonPath("$.soldTickets").value(eventDTO.getSoldTickets()))
                .andExpect(jsonPath("$.description").value(eventDTO.getDescription()))
                .andExpect(jsonPath("$.topic").value(eventDTO.getTopic().toString()))
                .andExpect(jsonPath("$.ageRestriction").value(eventDTO.isAgeRestriction()));
    }

    @Test
    void updateEvent() {
    }

    @Test
    void purchaseTicket() {
    }

    @Test
    void deleteEvent() {
    }

    @Test
    void likeEvent() {
    }

    @Test
    void unlikeEvent() {
    }

    @Test
    void getRecommendedEvents() {
    }
}