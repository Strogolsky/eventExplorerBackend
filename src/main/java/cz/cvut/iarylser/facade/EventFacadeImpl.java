package cz.cvut.iarylser.facade;

import cz.cvut.iarylser.dao.dto.EventDTO;
import cz.cvut.iarylser.dao.dto.PurchaseRequest;
import cz.cvut.iarylser.dao.dto.TicketResponse;
import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.entity.UserStatus;
import cz.cvut.iarylser.dao.mappersDto.EventMapperDTO;
import cz.cvut.iarylser.service.AuthService;
import cz.cvut.iarylser.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public EventDTO create(EventDTO dto) throws IllegalStateException {
        Event entity = eventMapper.toEntity(dto);
        User currentUser = authService.getUser();
        if(currentUser.getUserStatus() == UserStatus.BLOCKED) {
            log.warn("User is blocked and cannot create posts");
            throw new IllegalStateException("User is blocked and cannot create posts");
        }
        entity.setOrganizer(currentUser.getUsername());
        Event result = eventService.create(entity);
        return eventMapper.toDTO(result);
    }

    @Override
    public EventDTO update(Long id, EventDTO dto) throws EntityNotFoundException, IllegalStateException, IllegalAccessException {
        User currentUser = authService.getUser();
        if(currentUser.getUserStatus() == UserStatus.BLOCKED) {
            log.warn("User is blocked and cannot create posts");
            throw new IllegalStateException("User is blocked and cannot create posts");
        }
        if(!Objects.equals(dto.getOrganizer(), currentUser.getUsername())) {
            log.warn("User {} is not the same as the organizer.", currentUser.getUsername());
            throw new IllegalAccessException("User" + currentUser.getUsername() + " is not the same as the organizer.");
        }
        Event entity = eventMapper.toEntity(dto);
        Event result = eventService.update(id, entity);
        return eventMapper.toDTO(result);
    }

    @Override
    public boolean delete(Long id) throws IllegalAccessException, IllegalStateException {
        User currentUser = authService.getUser();
        if(currentUser.getUserStatus() == UserStatus.BLOCKED) {
            log.warn("User is blocked and cannot create posts");
            throw new IllegalStateException("User is blocked and cannot create posts");
        }
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
    public List<TicketResponse> purchaseTicket(Long eventId, PurchaseRequest request) throws EntityNotFoundException, IllegalStateException {
        User currentUser = authService.getUser();
        if(currentUser.getUserStatus() == UserStatus.BLOCKED) {
            log.warn("User is blocked and cannot create posts");
            throw new IllegalStateException("User is blocked and cannot create posts");
        }
        request.setCustomer(currentUser.getUsername());

        return eventService.purchaseTicket(eventId,request);
    }

    @Override
    public boolean like(Long eventId, Long userId) {
        User currentUser = authService.getUser();
        if(currentUser.getUserStatus() == UserStatus.BLOCKED) {
            log.warn("User is blocked and cannot create posts");
            throw new IllegalStateException("User is blocked and cannot create posts");
        }
        return eventService.like(eventId, currentUser.getId());
    }

    @Override
    public boolean unlike(Long eventId, Long userId) { // todo fix userId
        User currentUser = authService.getUser();
        if(currentUser.getUserStatus() == UserStatus.BLOCKED) {
            log.warn("User is blocked and cannot create posts");
            throw new IllegalStateException("User is blocked and cannot create posts");
        }
        return eventService.unlike(eventId,currentUser.getId());
    }
}
