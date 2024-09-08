package cz.cvut.iarylser.dao.mappersDTO;

import cz.cvut.iarylser.dao.DTO.EventDTO;
import cz.cvut.iarylser.dao.entity.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@Slf4j
public class EventMapperDTO implements MapperDTO<EventDTO, Event> {
    @Override
    public EventDTO toDTO(Event entity) {
        if(entity == null){
            log.warn("Attempted to convert null Event entity to DTO");
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
        log.info("Converted Event entity to DTO: {}", dto);
        return dto;
    }

    @Override
    public List<EventDTO> toDTOList(List<Event> entities) {
        if(entities == null){
            log.warn("Attempted to convert null list of Event entities to DTO list");
            return new ArrayList<>();
        }
        log.info("Converting list of Event entities to DTOs, size: {}", entities.size());
        List<EventDTO> dtos = new ArrayList<>();
        for (Event event : entities) {
            dtos.add(toDTO(event));
        }
        log.info("Converted {} Event entities to DTOs", dtos.size());
        return dtos;
    }

    @Override
    public Event toEntity(EventDTO dto) {
        if(dto == null) {
            log.warn("Attempted to convert null EventDTO to entity");
            return null;
        }
        Event entity = new Event();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDateAndTime(dto.getDateAndTime());
        entity.setTicketPrice(dto.getTicketPrice());
        entity.setLocation(dto.getLocation());
        entity.setCapacity(dto.getCapacity());
        entity.setSoldTickets(dto.getSoldTickets());
        entity.setDescription(dto.getDescription());
        entity.setTopic(dto.getTopic());
        entity.setAgeRestriction(dto.isAgeRestriction());
        entity.setOrganizer(dto.getOrganizer());

        log.info("Converted EventDTO to entity: {}", entity);
        return entity;
    }
}
