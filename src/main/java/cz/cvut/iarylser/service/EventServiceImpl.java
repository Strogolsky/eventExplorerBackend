package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.dto.TicketResponse;
import cz.cvut.iarylser.dao.dto.PurchaseRequest;
import cz.cvut.iarylser.dao.entity.*;
import cz.cvut.iarylser.dao.mappersDto.TicketMapperDTO;
import cz.cvut.iarylser.dao.repository.EventRepository;
import cz.cvut.iarylser.dao.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TicketServiceImpl ticketService;
    private final TicketMapperDTO ticketMapperDTO;


    @Override
    @Cacheable(value = "event")
    public List<Event> getAll() {
        log.info("Fetching all events");
        return eventRepository.findAll();
    }
    @Override
    @Cacheable(value = "event", key = "#eventId")
    public Event getById(Long eventId) {
        log.info("Fetching event with id: {}", eventId);
        return eventRepository.findById(eventId).orElse(null);
    }
    @Override
    public Event create(Event newEvent) {
        log.info("Creating new event: {}", newEvent);
        User organizer = userRepository.findByUsername(newEvent.getOrganizer());
        if (organizer == null) {
            log.warn("Organizer with nickname {} not found", newEvent.getOrganizer());
            return null;
        }
        newEvent.setOrganizerId(organizer.getId());
        newEvent.setUser(organizer);
        organizer.getCreatedEvents().add(newEvent);
        userRepository.save(organizer);
        Event createdEvent = eventRepository.save(newEvent);
        log.info("Created event: {}", createdEvent);
        return createdEvent;
    }

    @Override
    public List<TicketResponse> purchaseTicket(Long eventId, PurchaseRequest request) throws EntityNotFoundException, IllegalStateException {
        log.info("Purchasing {} tickets for event with id: {} for customer: {}", request.getQuantity(), eventId, request.getCustomer());

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event with id " + eventId + " not found"));
        User customer = userRepository.findByUsername(request.getCustomer());
        if (customer == null) {
            throw new EntityNotFoundException("User with nickname " + request.getCustomer() + " not found");
        }

        if (request.getQuantity() > event.getAvailableSeat()) {
            throw new IllegalStateException("Not enough available seats for event with id: " + eventId);
        }

        if (event.isAgeRestriction() && customer.getAge() < 18) {
            throw new IllegalStateException("User " + customer.getUsername() + " does not meet the age requirement for event " + eventId);
        }

        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < request.getQuantity(); i++) {
            Ticket ticket = ticketService.create(event, customer);
            tickets.add(ticket);
            event.setSoldTickets(event.getSoldTickets() + 1);
        }

        userRepository.save(customer);
        eventRepository.save(event);

        log.info("The tickets were purchased");
        return ticketMapperDTO.toDTOList(tickets);
    }
    @Override
    @Cacheable(value = "event", key = "#userId")
    public List<Event> getByUserId(Long userId) {
        log.info("Fetching events for user with id: {}", userId);
        return eventRepository.findByOrganizerId(userId);
    }
    @Override
    @CachePut(value = "event", key = "#eventId")
    public Event update(Long eventId, Event updatedEvent) throws EntityNotFoundException, IllegalStateException {
        log.info("Updating event with id: {}", eventId);
        Event existingEvent = getById(eventId);
        if (existingEvent == null) {
            log.warn("Event with id {} not found for update", eventId);
            throw new EntityNotFoundException("Event with id " + eventId + " not found");
        }
        if (!existingEvent.updateCapacity(updatedEvent.getCapacity())) {
            log.warn("Attempted to set capacity to {}, but there are {} sold tickets",
                    updatedEvent.getCapacity(), updatedEvent.getSoldTickets());
            throw new IllegalStateException("Cannot update capacity because there are sold tickets");
        }
        if (updatedEvent.getTitle() != null && !updatedEvent.getTitle().isEmpty()) {
            existingEvent.setTitle(updatedEvent.getTitle());
        }

        if (updatedEvent.getDateAndTime() != null) {
            existingEvent.setDateAndTime(updatedEvent.getDateAndTime());
        }

        if (updatedEvent.getDescription() != null && !updatedEvent.getDescription().isEmpty()) {
            existingEvent.setDescription(updatedEvent.getDescription());
        }

        if (updatedEvent.getLocation() != null && !updatedEvent.getLocation().isEmpty()) {
            existingEvent.setLocation(updatedEvent.getLocation());
        }

        if (updatedEvent.getTicketPrice() >= 0) {
            existingEvent.setTicketPrice(updatedEvent.getTicketPrice());
        }

        existingEvent.setAgeRestriction(updatedEvent.isAgeRestriction());

        updateRelatedTickets(existingEvent);
        log.info("The event is updated");
        return eventRepository.save(existingEvent);
    }
    @Override
    public void updateForOrgChange(Event event, User organizer) {
        log.info("Updating organizer for event with id: {} to {}", event.getId(), organizer.getUsername());
        event.setOrganizer(organizer.getUsername());
        eventRepository.save(event);
    }
    @Override
    @CacheEvict(value = "event", key = "#eventId")
    public boolean delete(Long eventId) {
        log.info("Deleting event with id: {}", eventId);
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            log.warn("Event with id {} not found for deletion", eventId);
            return false;
        }

        Long idOrganizer = event.getOrganizerId();
        User author = userRepository.findById(idOrganizer).orElse(null);
        if (author != null) {
            author.getCreatedEvents().remove(event);
            userRepository.save(author);
        } else {
            log.warn("Author with nickname {} not found when deleting event {}", event.getOrganizer(), eventId);
            return false;
        }
        eventRepository.deleteById(eventId);
        log.info("Deleted event with id: {}", eventId);
        return true;
    }
    private void updateRelatedTickets(Event event) {
        log.info("Updating related tickets for event with id: {}", event.getId());
        Ticket updatedTicket = new Ticket();
        updatedTicket.setDetails(event.getDescription());
        for( Ticket ticket : event.getTickets()){
            ticketService.update(ticket.getId(),updatedTicket);
        }
    }
    @Override
    @CachePut(value = "event", key = "#eventId")
    public boolean like(Long eventId, Long userId) {
        log.info("User with id: {} liking event with id: {}", userId, eventId);
        User user = userRepository.findById(userId).orElse(null);
        Event event = eventRepository.findById(eventId).orElse(null);
        if (user == null || event == null) {
            log.warn("User or Event not found for like operation");
            return false;
        }
        user.getLikeByMe().add(event);
        event.getLikeBy().add(user);
        userRepository.save(user);
        eventRepository.save(event);
        log.info("User with id: {} liked event with id: {}", userId, eventId);
        return true;
    }
    @Override
    @CachePut(value = "event", key = "#eventId")
    public boolean unlike(Long eventId, Long userId) {
        log.info("User with id: {} unliking event with id: {}", userId, eventId);
        User user = userRepository.findById(userId).orElse(null);
        Event event = eventRepository.findById(eventId).orElse(null);
        if (user == null || event == null) {
            log.warn("User or Event not found for unlike operation");
            return false;
        }
        user.getLikeByMe().remove(event);
        event.getLikeBy().remove(user);
        userRepository.save(user);
        eventRepository.save(event);
        log.info("User with id: {} unliked event with id: {}", userId, eventId);
        return true;
    }
}
