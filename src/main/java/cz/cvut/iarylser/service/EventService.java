package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.DTO.EventDTO;
import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.DTO.PurchaseRequest;
import cz.cvut.iarylser.dao.entity.*;
import cz.cvut.iarylser.dao.repository.EventRepository;
import cz.cvut.iarylser.dao.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class EventService implements CrudService<Event,Long> {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TicketService ticketService;

    public EventService(EventRepository eventRepository, UserRepository userRepository, TicketService ticketService) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.ticketService = ticketService;
    }
    @Override
    public List<Event> getAll(){
        return eventRepository.findAll();
    }
    @Override
    public Event getById(Long eventId){
        return eventRepository.findById(eventId).orElse(null);
    }
    @Override
    public Event create(Event newEvent){
        User organizer = userRepository.findByNickname(newEvent.getOrganizer());
        if (organizer == null) {
            return null;
        }
        newEvent.setIdOrganizer(organizer.getId());
        newEvent.setUser(organizer);
        organizer.getCreatedEvents().add(newEvent);
        userRepository.save(organizer);
        return eventRepository.save(newEvent);
    }
    public List<TicketDTO> purchaseTicket(Long eventId, PurchaseRequest request){
        Event event = eventRepository.findById(eventId).orElse(null);
        User customer = userRepository.findByNickname(request.getCustomer());
        if (customer == null || event == null) {
            log.warn("User or event with nickname {} not found", request.getCustomer());
            return null;
        }
        if(request.getQuantity() <= event.getAvailableSeat()){
            if (event.isAgeRestriction() && customer.getAge() < 18) {
                log.warn("User {} does not meet the age requirement for event {}", customer.getNickname(), eventId);
                return null;
            }
            List<Ticket> tickets = new ArrayList<>();
            for(int i = 0; i < request.getQuantity(); i++){
                Ticket ticket = ticketService.createTicket(event, customer);
                tickets.add(ticket);
                event.setSoldTickets(event.getSoldTickets() + 1);

            }
            userRepository.save(customer);
            eventRepository.save(event);
            return ticketService.convertTicketsToDTOs(tickets);
        }
        return null;
    }

    public List<Event> getEventsByUserId(Long userId) {
        return eventRepository.findByIdOrganizer(userId);
    }
    @Override
    public Event update(Long eventId, Event updatedEvent){
        Event existingEvent = getById(eventId);
        if (existingEvent == null) {
            log.warn("Event with id {} not found for update", eventId);
            return null;
        }
        if(!existingEvent.updateCapacity(updatedEvent.getCapacity())) {
            log.warn("Attempted to set capacity to {}, but there are {} sold tickets", updatedEvent.getCapacity(), updatedEvent.getSoldTickets());
            return null;
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
        return eventRepository.save(existingEvent);
    }

    public void updateForOrgChange(Event event, User organizer){
        event.setOrganizer(organizer.getNickname());

        eventRepository.save(event);
    }
    @Override
    public boolean delete(Long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            log.warn("Event with id {} not found for deletion", eventId);
            return false;
        }

        Long idOrganizer = event.getIdOrganizer();
        User author = userRepository.findById(idOrganizer).orElse(null);
        if (author != null) {
            author.getCreatedEvents().remove(event);
            userRepository.save(author);
        } else {
            log.warn("Author with nickname {} not found when deleting event {}", event.getOrganizer(), eventId);
            return false;
        }
        eventRepository.deleteById(eventId);
        return true;
    }

    public EventDTO convertToDto(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDateAndTime(event.getDateAndTime());
        dto.setTicketPrice(event.getTicketPrice());
        dto.setLocation(event.getLocation());
        dto.setLikes(event.getLikeBy().size());
        dto.setCapacity(event.getCapacity());
        dto.setSoldTickets(event.getSoldTickets());
        dto.setDescription(event.getDescription());
        dto.setTopic(event.getTopic());
        dto.setAgeRestriction(event.isAgeRestriction());
        dto.setOrganizer(event.getOrganizer());

        return dto;
    }
    public List<EventDTO> convertToDTOList(List<Event> events) {
        List<EventDTO> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(convertToDto(event));
        }
        return dtos;
    }
    private void updateRelatedTickets(Event event){
        Ticket updatedTicket = new Ticket();
        updatedTicket.setDetails(event.getDescription());
        for( Ticket ticket : event.getTickets()){
            ticketService.updateTicket(ticket.getId(),updatedTicket);
        }
    }
    public boolean likeEvent(Long eventId, Long userId){
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
        return true;
    }
    public boolean unlikeEvent(Long eventId, Long userId){
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
        return true;
    }

    public List<Event> getByLikedGreaterThan(int likes) {
        Collection<Event> eventsCollection = eventRepository.findByLikedGreaterThan(likes);


        return new ArrayList<>(eventsCollection);
    }
}
