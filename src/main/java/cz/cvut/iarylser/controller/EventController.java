package cz.cvut.iarylser.controller;


import cz.cvut.iarylser.dao.DTO.EventDTO;
import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.DTO.PurchaseRequest;
import cz.cvut.iarylser.dao.entity.*;
import cz.cvut.iarylser.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.naming.AuthenticationException;

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
    @Operation(summary = "Get all events",
            description = "Retrieves a list of all events available in the system.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all events",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EventDTO.class)))
    public ResponseEntity<List<EventDTO>> getAllEvents(){
        List<Event> result = eventService.getAllEvents();
        return ResponseEntity.ok(eventService.convertToDTOList(result));
    }
    @GetMapping("/{eventId}")
    @Operation(summary = "Get event by ID",
            description = "Retrieves the details of an event by its ID.")
    @ApiResponse(responseCode = "200", description = "Event found and returned",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EventDTO.class)))
    @ApiResponse(responseCode = "404", description = "Event not found")
    public ResponseEntity<?>getEventById(
            @Parameter(description = "ID of the event to retrieve", required = true)
            @PathVariable Long eventId){
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventService.convertToDto(event));
    }
    @PostMapping
    @Operation(summary = "Create new event",
            description = "Creates a new event with the given details.")
    @ApiResponse(responseCode = "200", description = "Event created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EventDTO.class)))
    public ResponseEntity<?> createEvent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Event data to create a new event", required = true)
            @RequestBody Event newEvent) {
            Event event = eventService.createEvent(newEvent);
            if (event == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(eventService.convertToDto(event));
    }
    @PutMapping("/{eventId}")
    @Operation(summary = "Update event",
            description = "Updates the details of an existing event by its ID.")
    @ApiResponse(responseCode = "200", description = "Event updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EventDTO.class)))
    @ApiResponse(responseCode = "404", description = "Event not found")
    public ResponseEntity<EventDTO> updateEvent(
            @Parameter(description = "ID of the event to update", required = true)
            @PathVariable Long eventId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated event data", required = true)
            @RequestBody Event updatedEvent){
        Event event = eventService.updateEvent(eventId, updatedEvent);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventService.convertToDto(event));
    }
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get events by user ID",
            description = "Retrieves a list of all events associated with a given user ID.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved events for the user",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = EventDTO.class))))
    public ResponseEntity<List<EventDTO>> getEventsByUserId(
            @Parameter(description = "User ID to retrieve events for", required = true)
            @PathVariable Long userId) {
        List<Event> events = eventService.getEventsByUserId(userId);

        List<EventDTO> eventDTOs = eventService.convertToDTOList(events);

        return ResponseEntity.ok(eventDTOs);
    }

    @PostMapping("/{eventId}/purchase")
    @Operation(summary = "Purchase ticket for an event",
            description = "Processes a ticket purchase for the specified event.")
    @ApiResponse(responseCode = "200", description = "Ticket purchased successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TicketDTO.class)))
    @ApiResponse(responseCode = "400", description = "Purchase failed due to invalid data or other issues")
    public ResponseEntity<?> purchaseTicket(
            @Parameter(description = "ID of the event to purchase tickets for", required = true)
            @PathVariable Long eventId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Purchase request details", required = true)
            @RequestBody PurchaseRequest request) {
        List<TicketDTO> tickets = eventService.purchaseTicket(eventId, request);
        if (tickets == null) {
            return ResponseEntity.badRequest().body("Purchase failed due to invalid data or other issues.");
        }
        return ResponseEntity.ok(tickets);
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "Delete an event",
            description = "Deletes the event with the specified ID.")
    @ApiResponse(responseCode = "204", description = "Event deleted successfully")
    @ApiResponse(responseCode = "404", description = "Event not found")
    public ResponseEntity<?> deleteEvent(
            @Parameter(description = "ID of the event to be deleted", required = true)
            @PathVariable Long eventId){
        boolean isDeleted = eventService.deleteEvent(eventId);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{eventId}/like/{userId}")
    @Operation(summary = "Like an event",
            description = "Marks the event as liked by the user.")
    @ApiResponse(responseCode = "200", description = "Event liked successfully")
    @ApiResponse(responseCode = "404", description = "Event or user not found")
    public ResponseEntity<?> likeEvent(
            @Parameter(description = "ID of the event to like", required = true)
            @PathVariable Long eventId,
            @Parameter(description = "ID of the user who is liking the event", required = true)
            @PathVariable Long userId) {
        boolean result = eventService.likeEvent(eventId, userId);
        if (!result) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{eventId}/unlike/{userId}")
    @Operation(summary = "Unlike an event",
            description = "Removes the like mark from the event by the user.")
    @ApiResponse(responseCode = "200", description = "Event unliked successfully")
    @ApiResponse(responseCode = "404", description = "Event or user not found")
    public ResponseEntity<?> unlikeEvent(
            @Parameter(description = "ID of the event to unlike", required = true)
            @PathVariable Long eventId,
            @Parameter(description = "ID of the user who is unliking the event", required = true)
            @PathVariable Long userId) {
        boolean result = eventService.unlikeEvent(eventId, userId);
        if (!result) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
    @GetMapping("/likes/{likes}")
    @Operation(summary = "Get events by likes",
            description = "Retrieves a list of events where the number of likes is greater than the specified threshold.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved events",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EventDTO.class)))
    public ResponseEntity<List<EventDTO>> getByLikedGreaterThan(
            @Parameter(description = "The minimum number of likes for events to be retrieved", required = true)
            @PathVariable int likes){
        List<Event> result = eventService.getByLikedGreaterThan(likes);
        List<EventDTO> dtoList = eventService.convertToDTOList(result);
        return ResponseEntity.ok(dtoList);
    }

}
