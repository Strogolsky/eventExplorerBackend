package cz.cvut.iarylser.dao.mappersDTO;

import cz.cvut.iarylser.dao.DTO.EventDTO;
import cz.cvut.iarylser.dao.entity.Event;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class EventMapperDTO implements MapperDTO<EventDTO, Event> {
    @Override
    public EventDTO toDTO(Event entity) {
        if(entity == null){
            return null;
        }

        EventDTO dto = new EventDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDateAndTime(entity.getDateAndTime());
        dto.setTicketPrice(entity.getTicketPrice());
        dto.setLocation(entity.getLocation());
        dto.setLikes(entity.getLikeBy().size());
        dto.setCapacity(entity.getCapacity());
        dto.setSoldTickets(entity.getSoldTickets());
        dto.setDescription(entity.getDescription());
        dto.setTopic(entity.getTopic());
        dto.setAgeRestriction(entity.isAgeRestriction());
        dto.setOrganizer(entity.getOrganizer());
        return dto;
    }

    @Override
    public List<EventDTO> toDTOList(List<Event> entities) {
        List<EventDTO> dtos = new ArrayList<>();
        for (Event event : entities) {
            dtos.add(toDTO(event));
        }
        return dtos;
    }

    @Override
    public Event toEntity(EventDTO dto) {
        if(dto == null) {
            return null;
        }

        Event entity = new Event();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDateAndTime(entity.getDateAndTime());
        entity.setTicketPrice(entity.getTicketPrice());
        entity.setLocation(entity.getLocation());
        entity.setCapacity(entity.getCapacity());
        entity.setSoldTickets(entity.getSoldTickets());
        entity.setDescription(entity.getDescription());
        entity.setTopic(entity.getTopic());
        entity.setAgeRestriction(entity.isAgeRestriction());
        entity.setOrganizer(entity.getOrganizer());
        return entity;
    }
}
