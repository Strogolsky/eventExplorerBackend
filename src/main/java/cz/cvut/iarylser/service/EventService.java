package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.DTO.EventDTO;
import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.DTO.TicketPurchaseRequest;
import cz.cvut.iarylser.dao.entity.*;
import cz.cvut.iarylser.dao.repository.EventRepository;
import cz.cvut.iarylser.dao.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TicketService ticketService;

    public EventService(EventRepository eventRepository, UserRepository userRepository, TicketService ticketService) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.ticketService = ticketService;
    }

    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }

    public Event getEventById(Long eventId){
        return eventRepository.findById(eventId).orElse(null);
    }

    public Event createEvent(Event newEvent){
        User organizer = userRepository.findByNickname(newEvent.getOrganizer());
        newEvent.setIdOrganizer(organizer.getId());
        organizer.getCreatedEvents().add(newEvent);
        userRepository.save(organizer);
        return eventRepository.save(newEvent);
    }
    public List<TicketDTO> purchaseTicket(Long eventId, TicketPurchaseRequest request){
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


    public Event updateEvent(Long eventId, Event updatedEvent){
        Event existingEvent = getEventById(eventId);
        if (existingEvent == null) {
            log.warn("Event with id {} not found for update", eventId);
            return null;
        }
        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setDateAndTime(updatedEvent.getDateAndTime());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setLocation(updatedEvent.getLocation());
        existingEvent.setTicketPrice(updatedEvent.getTicketPrice());
        existingEvent.updateCapacity(updatedEvent.getCapacity());
        existingEvent.setAgeRestriction(existingEvent.isAgeRestriction());

        updateRelatedTickets(existingEvent);
        return eventRepository.save(existingEvent);
    }

    public void updateForOrgChange(Event event, User organizer){
        event.setOrganizer(organizer.getNickname());

        eventRepository.save(event);
    }
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            log.warn("Event with id {} not found for deletion", eventId);
            return;
        }

        ticketService.deactivateTickets(event);

        String organizerNickname = event.getOrganizer();
        User author = userRepository.findByNickname(organizerNickname);
        if (author != null) {
            author.getCreatedEvents().remove(event);
            userRepository.save(author);
        } else {
            log.warn("Author with nickname {} not found when deleting event {}", organizerNickname, eventId);
        }
        eventRepository.deleteById(eventId);
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
        updatedTicket.setDetails(event.getDescription()); // todo change
        for( Ticket ticket : event.getTickets()){
            ticketService.updateTicket(ticket.getId(),updatedTicket);
        }
    }
    public void likeEvent(Long eventId, Long userId){ // todo change on boolean
        User user = userRepository.findById(userId).orElse(null);
        Event event = eventRepository.findById(eventId).orElse(null);
        if (user == null || event == null) {
            log.warn("User or Event not found for like operation");
            return;
        }
        user.getLikeByMe().add(event);
        event.getLikeBy().add(user);
        userRepository.save(user);
        eventRepository.save(event);
    }
    public void unlikeEvent(Long eventId, Long userId){
        User user = userRepository.findById(userId).orElse(null);
        Event event = eventRepository.findById(eventId).orElse(null);
        if (user == null || event == null) {
            log.warn("User or Event not found for unlike operation");
            return;
        }
        user.getLikeByMe().remove(event);
        event.getLikeBy().remove(user);
        userRepository.save(user);
        eventRepository.save(event);
    }

}
