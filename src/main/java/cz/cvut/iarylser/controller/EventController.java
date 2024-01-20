package cz.cvut.iarylser.controller;

import cz.cvut.iarylser.dao.DTO.EventDTO;
import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.DTO.PurchaseRequest;
import cz.cvut.iarylser.dao.entity.*;
import cz.cvut.iarylser.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
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
            return ResponseEntity.notFound().build(); // todo change
        }
        return ResponseEntity.ok(eventService.convertToDto(event));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EventDTO>> getEventsByUserId(@PathVariable Long userId) {
        List<Event> events = eventService.getEventsByUserId(userId);

        List<EventDTO> eventDTOs = eventService.convertToDTOList(events);

        return ResponseEntity.ok(eventDTOs);
    }

    @PostMapping("/{eventId}/purchase")
    public ResponseEntity<List<TicketDTO>> purchaseTicket(@PathVariable Long eventId, @RequestBody PurchaseRequest request) {
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
    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<List<EventDTO>> getRecommendedEvents(@PathVariable Long userId){
        List<Event> result = eventService.getRecommend(userId);
        List<EventDTO> dtoList = eventService.convertToDTOList(result);
        return ResponseEntity.ok(dtoList);
    }

}
