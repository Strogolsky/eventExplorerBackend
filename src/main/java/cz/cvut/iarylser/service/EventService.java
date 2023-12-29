package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.repository.EventRepository;
import cz.cvut.iarylser.dao.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
public class EventService {
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
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

    public Event updateEvent(Long eventId, Event updatedEvent){
        Event existingEvent = getEventById(eventId);
        // todo check
        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setDateAndTime(updatedEvent.getDateAndTime());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setLocation(updatedEvent.getLocation());
        existingEvent.setTicketPrice(updatedEvent.getTicketPrice());
        existingEvent.setCapacity(updatedEvent.getCapacity());
        existingEvent.setAgeRestriction(existingEvent.isAgeRestriction());

        return eventRepository.save(existingEvent);
    }
    public void deleteEvent(Long eventId){
        Event event = eventRepository.findById(eventId).orElse(null);
        User author = userRepository.findByNickname(event.getOrganizer());
        author.getCreatedEvents().remove(event);
        userRepository.save(author);
        eventRepository.deleteById(eventId);
    }

}
