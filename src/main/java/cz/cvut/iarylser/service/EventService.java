package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.dao.repository.EventRepository;
import cz.cvut.iarylser.dao.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EventService {
    private EventRepository eventRepository;
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
        eventRepository.deleteById(eventId);
    }

}
