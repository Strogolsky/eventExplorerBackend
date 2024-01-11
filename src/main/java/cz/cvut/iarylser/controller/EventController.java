package cz.cvut.iarylser.controller;

import cz.cvut.iarylser.dao.DTO.EventDTO;
import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.DTO.TicketPurchaseRequest;
import cz.cvut.iarylser.dao.entity.*;
import cz.cvut.iarylser.service.EventService;
import cz.cvut.iarylser.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/event")
public class EventController {
    private final EventService eventService;
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents(){
        List<Event> result = eventService.getAllEvents();
        return ResponseEntity.ok(eventService.convertToDTOList(result));
    }
    @GetMapping("/{eventId}")
    public ResponseEntity<EventDTO>getEventById(@PathVariable Long eventId){
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventService.convertToDto(event));
    }
    @PostMapping
    public ResponseEntity<EventDTO>createEvent(@RequestBody Event newEvent)
    {
        Event event = eventService.createEvent(newEvent);
        return ResponseEntity.ok(eventService.convertToDto(event));
    }
    @PutMapping("/{eventId}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long eventId, @RequestBody Event updatedEvent){
        Event event = eventService.updateEvent(eventId, updatedEvent);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventService.convertToDto(event));
    }
    @PostMapping("/{eventId}/purchase")
    public ResponseEntity<List<TicketDTO>> purchaseTicket(@PathVariable Long eventId, @RequestBody TicketPurchaseRequest request) {
        List<TicketDTO> tickets = eventService.purchaseTicket(eventId, request);
        return ResponseEntity.ok(tickets);
    }
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId){
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{eventId}/like/{userId}")
    public ResponseEntity<Void> likeEvent(@PathVariable Long eventId, @PathVariable Long userId) {
        eventService.likeEvent(eventId, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{eventId}/unlike/{userId}")
    public ResponseEntity<Void> unlikeEvent(@PathVariable Long eventId, @PathVariable Long userId) {
        eventService.unlikeEvent(eventId, userId);
        return ResponseEntity.noContent().build();
    }

}
