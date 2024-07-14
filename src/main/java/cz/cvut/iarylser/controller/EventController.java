package cz.cvut.iarylser.controller;


import cz.cvut.iarylser.dao.DTO.EventDTO;
import cz.cvut.iarylser.dao.DTO.LikeRequest;
import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.DTO.PurchaseRequest;
import cz.cvut.iarylser.dao.entity.*;
import cz.cvut.iarylser.dao.mappersDTO.EventMapperDTO;
import cz.cvut.iarylser.facade.EventFacade;
import cz.cvut.iarylser.facade.EventFacadeImpl;
import cz.cvut.iarylser.service.EventServiceImpl;
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

import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping(value = "/events")
public class EventController {
    private final EventFacadeImpl eventFacade;

    @Autowired
    public EventController(EventFacadeImpl eventFacade) {
        this.eventFacade = eventFacade;
    }
    @GetMapping
    @Operation(summary = "Get all events",
            description = "Retrieves a list of all events available in the system.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all events",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EventDTO.class)))
    public ResponseEntity<List<EventDTO>> getAll(){
        List<EventDTO> result = eventFacade.getAll();
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{eventId}")
    @Operation(summary = "Get event by ID",
            description = "Retrieves the details of an event by its ID.")
    @ApiResponse(responseCode = "200", description = "Event found and returned",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EventDTO.class)))
    @ApiResponse(responseCode = "404", description = "Event not found")
    public ResponseEntity<?>getById(
            @Parameter(description = "ID of the event to retrieve", required = true)
            @PathVariable Long eventId){
        EventDTO result = eventFacade.getById(eventId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
    @PostMapping
    @Operation(summary = "Create new event",
            description = "Creates a new event with the given details.")
    @ApiResponse(responseCode = "200", description = "Event created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EventDTO.class)))
    public ResponseEntity<?> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Event data to create a new event", required = true)
            @RequestBody EventDTO newEvent) {
            EventDTO result = eventFacade.create(newEvent);
            if (result == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(result);
    }
    @PutMapping("/{eventId}")
    @Operation(summary = "Update event",
            description = "Updates the details of an existing event by its ID.")
    @ApiResponse(responseCode = "200", description = "Event updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EventDTO.class)))
    @ApiResponse(responseCode = "404", description = "Event not found")
    public ResponseEntity<EventDTO> update(
            @Parameter(description = "ID of the event to update", required = true)
            @PathVariable Long eventId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated event data", required = true)
            @RequestBody EventDTO updatedEvent){
        EventDTO result = eventFacade.update(eventId, updatedEvent);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get events by user ID",
            description = "Retrieves a list of all events associated with a given user ID.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved events for the user",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = EventDTO.class))))
    public ResponseEntity<List<EventDTO>> getByUserId(
            @Parameter(description = "User ID to retrieve events for", required = true)
            @PathVariable Long userId) {
        List<EventDTO> result = eventFacade.getByUserId(userId);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/{eventId}/purchase")
    @Operation(summary = "Purchase ticket for an event",
            description = "Processes a ticket purchase for the specified event.")
    @ApiResponse(responseCode = "200", description = "Ticket purchased successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TicketDTO.class)))
    @ApiResponse(responseCode = "400", description = "Purchase failed due to invalid data or other issues")
    public ResponseEntity<?> purchase(
            @Parameter(description = "ID of the event to purchase tickets for", required = true)
            @PathVariable Long eventId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Purchase request details", required = true)
            @RequestBody PurchaseRequest request) {
        List<TicketDTO> result = eventFacade.purchaseTicket(eventId, request);
        if (result == null) {
            return ResponseEntity.badRequest().body("Purchase failed due to invalid data or other issues.");
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "Delete an event",
            description = "Deletes the event with the specified ID.")
    @ApiResponse(responseCode = "204", description = "Event deleted successfully")
    @ApiResponse(responseCode = "404", description = "Event not found")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID of the event to be deleted", required = true)
            @PathVariable Long eventId){
        if (!eventFacade.delete(eventId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/like")
    @Operation(summary = "Like an event",
            description = "Marks the event as liked by the user.")
    @ApiResponse(responseCode = "200", description = "Event liked successfully")
    @ApiResponse(responseCode = "404", description = "Event or user not found")
    public ResponseEntity<?> like(@RequestBody LikeRequest request) {
        if (!eventFacade.like(request.getEventId(), request.getUserId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/unlike")
    @Operation(summary = "Unlike an event",
            description = "Removes the like mark from the event by the user.")
    @ApiResponse(responseCode = "200", description = "Event unliked successfully")
    @ApiResponse(responseCode = "404", description = "Event or user not found")
    public ResponseEntity<?> unlike(
            @RequestBody LikeRequest request) {
        if (!eventFacade.unlike(request.getEventId(), request.getUserId())) {
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
        List<EventDTO> result = eventFacade.getByLikedGreaterThan(likes);
        return ResponseEntity.ok(result);
    }

}
