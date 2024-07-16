package cz.cvut.iarylser.facade;

import cz.cvut.iarylser.controller.EventController;
import cz.cvut.iarylser.dao.DTO.EventDTO;
import cz.cvut.iarylser.dao.DTO.PurchaseRequest;
import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.mappersDTO.EventMapperDTO;
import cz.cvut.iarylser.service.EventService;
import cz.cvut.iarylser.service.EventServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class EventFacadeImpl implements EventFacade {
    private final EventMapperDTO eventMapper;
    private final EventService eventService;

    public EventFacadeImpl(EventMapperDTO eventMapper, EventServiceImpl eventService) {
        this.eventMapper = eventMapper;
        this.eventService = eventService;
    }

    @Override
    public EventDTO create(EventDTO dto) {
        Event entity = eventMapper.toEntity(dto);
        Event result = eventService.create(entity);
        return eventMapper.toDTO(result);
    }

    @Override
    public EventDTO update(Long id, EventDTO dto) throws EntityNotFoundException, IllegalStateException {
        Event entity = eventMapper.toEntity(dto);
        Event result = eventService.update(id, entity);
        return eventMapper.toDTO(result);
    }

    @Override
    public boolean delete(Long id) {
        return eventService.delete(id);
    }

    @Override
    public List<EventDTO> getAll() {
        List<Event> result = eventService.getAll();
        return eventMapper.toDTOList(result);
    }

    @Override
    public EventDTO getById(Long id) {
        Event result = eventService.getById(id);
        return eventMapper.toDTO(result);
    }

    @Override
    public List<EventDTO> getByUserId(Long id) {
        List<Event> result = eventService.getByUserId(id);
        return eventMapper.toDTOList(result);
    }

    @Override
    public List<TicketDTO> purchaseTicket(Long eventId, PurchaseRequest request) throws EntityNotFoundException, IllegalStateException {
        return eventService.purchaseTicket(eventId,request);
    }

    @Override
    public boolean like(Long eventId, Long userId) {
        return eventService.like(eventId,userId);
    }

    @Override
    public boolean unlike(Long eventId, Long userId) {
        return eventService.unlike(eventId,userId);
    }

    @Override
    public List<EventDTO> getByLikedGreaterThan(int likes) {
        List<Event> result = eventService.getByLikedGreaterThan(likes);
        return eventMapper.toDTOList(result);
    }
}
