package cz.cvut.iarylser.facade;

import cz.cvut.iarylser.dao.DTO.EventDTO;
import cz.cvut.iarylser.dao.DTO.PurchaseRequest;
import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.mappersDTO.EventMapperDTO;
import cz.cvut.iarylser.service.AuthService;
import cz.cvut.iarylser.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventFacadeImpl implements EventFacade {
    private final EventMapperDTO eventMapper;
    private final EventService eventService;
    private final AuthService authService;

    @Override
    public EventDTO create(EventDTO dto) {
        Event entity = eventMapper.toEntity(dto);
        User currentUser = authService.getUser();
        entity.setOrganizer(currentUser.getUsername());
        Event result = eventService.create(entity);
        return eventMapper.toDTO(result);
    }

    @Override
    public EventDTO update(Long id, EventDTO dto) throws EntityNotFoundException, IllegalStateException, IllegalAccessException {
        User currentUser = authService.getUser();
        if(!Objects.equals(dto.getOrganizer(), currentUser.getUsername())) {
            log.warn("User {} is not the same as the organizer.", currentUser.getUsername());
            throw new IllegalAccessException("User" + currentUser.getUsername() + " is not the same as the organizer.");
        }
        Event entity = eventMapper.toEntity(dto);
        Event result = eventService.update(id, entity);
        return eventMapper.toDTO(result);
    }

    @Override
    public boolean delete(Long id) throws IllegalAccessException {
        User currentUser = authService.getUser();
        Event eventToDelete = eventService.getById(id);
        if(!Objects.equals(currentUser.getUsername(), eventToDelete.getOrganizer())) {
            log.warn("User {} is not the same as the organizer.", currentUser.getUsername());
            throw new IllegalAccessException("User" + currentUser.getUsername() + " is not the same as the organizer.");
        }
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
        User currentUser = authService.getUser();
        request.setCustomer(currentUser.getUsername());

        return eventService.purchaseTicket(eventId,request);
    }

    @Override
    public boolean like(Long eventId, Long userId) {
        User currentUser = authService.getUser();

        return eventService.like(eventId, currentUser.getId());
    }

    @Override
    public boolean unlike(Long eventId, Long userId) { // todo fix userId
        User currentUser = authService.getUser();
        return eventService.unlike(eventId,currentUser.getId());
    }

    @Override
    public List<EventDTO> getByLikedGreaterThan(int likes) {
        List<Event> result = eventService.getByLikedGreaterThan(likes);
        return eventMapper.toDTOList(result);
    }
}
