package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EventService {
    private EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }
}
