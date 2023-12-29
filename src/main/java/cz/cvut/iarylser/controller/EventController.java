package cz.cvut.iarylser.controller;

import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/event")
public class EventController {
    private EventService eventService;
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(){ // todo create DTO
        List<Event> result = eventService.getAllEvents();
        return ResponseEntity.ok(result);
    }
    // todo principal
    @GetMapping("/{eventId}")
    public ResponseEntity<Event>getEventById(@PathVariable Long eventId){
        Event event = eventService.getEventById(eventId);
        // todo check on null
        return ResponseEntity.ok(event);
    }
    @PostMapping
    public ResponseEntity<Event>createEvent(@RequestBody Event newEvent)
    {
        Event event = eventService.createEvent(newEvent);
        return ResponseEntity.ok(event);
    }
    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody Event updatedEvent){
        Event event = eventService.updateEvent(eventId, updatedEvent);
        // todo check
        return ResponseEntity.ok(event);
    }
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId){
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
