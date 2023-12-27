package cz.cvut.iarylser.controller;

import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.User;
import cz.cvut.iarylser.service.EventService;
import cz.cvut.iarylser.service.TicketService;
import cz.cvut.iarylser.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/event")
public class EventController {
    private EventService eventService;
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(){
        List<Event> result = eventService.getAllEvents();
        return ResponseEntity.ok(result);
    }

//    @PostMapping
//    public ResponseEntity<Event>createEvent(@RequestBody User newEvent){
//        Event event = eventService.createEvent(newEvent);
//        // todo check
//        return ResponseEntity.ok(event);
}
