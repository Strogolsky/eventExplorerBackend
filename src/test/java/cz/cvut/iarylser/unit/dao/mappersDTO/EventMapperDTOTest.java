package cz.cvut.iarylser.unit.dao.mappersDTO;

import cz.cvut.iarylser.dao.DTO.EventDTO;
import cz.cvut.iarylser.dao.DTO.UserDTO;
import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.Topics;
import cz.cvut.iarylser.dao.mappersDTO.EventMapperDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventMapperDTOTest {
    private EventMapperDTO eventMapperDTO;
    private Event event;
    private EventDTO dto;

    @BeforeEach
    void setUp() {
        eventMapperDTO = new EventMapperDTO();
        event = new Event();
        dto = new EventDTO();

        event.setId(1L);
        event.setTitle("Title");
        event.setDateAndTime(LocalDateTime.MIN);
        event.setTicketPrice(1000);
        event.setLocation("Location");
        event.setCapacity(10);
        event.setSoldTickets(10);
        event.setDescription("Desciption");
        event.setTopic(Topics.FAMILY);
        event.setAgeRestriction(true);
        event.setOrganizer("Organizer");

        dto.setId(1L);
        dto.setTitle("Title");
        dto.setDateAndTime(LocalDateTime.MIN);
        dto.setTicketPrice(1000);
        dto.setLocation("Location");
        dto.setCapacity(10);
        dto.setSoldTickets(10);
        dto.setDescription("Desciption");
        dto.setTopic(Topics.FAMILY);
        dto.setAgeRestriction(true);
        dto.setOrganizer("Organizer");



    }

    @Test
    void toDTO() {
        EventDTO newDto = eventMapperDTO.toDTO(event);
        assertEquals(dto,newDto);
    }

    @Test
    void toDTOList() {
    }

    @Test
    void toEntity() {
        Event newEvent = eventMapperDTO.toEntity(dto);
        equalEntity(newEvent, event);
    }
    private void equalEntity(Event left, Event right) {
        assertEquals(left.getId(),right.getId());
        assertEquals(left.getTitle(),right.getTitle());
        assertEquals(left.getDateAndTime(),right.getDateAndTime());
        assertEquals(left.getTicketPrice(),right.getTicketPrice());
        assertEquals(left.getLocation(),right.getLocation());
        assertEquals(left.getCapacity(),right.getCapacity());
        assertEquals(left.getSoldTickets(),right.getSoldTickets());
        assertEquals(left.getDescription(),right.getDescription());
        assertEquals(left.getOrganizer(),right.getOrganizer());
        assertEquals(left.getTopic(),right.getTopic());
    }
}