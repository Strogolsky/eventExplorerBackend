package cz.cvut.iarylser.controller;


import cz.cvut.iarylser.dao.dto.EventDTO;
import cz.cvut.iarylser.dao.dto.LikeRequest;
import cz.cvut.iarylser.dao.dto.TicketResponse;
import cz.cvut.iarylser.dao.dto.PurchaseRequest;
import cz.cvut.iarylser.facade.EventFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping(value = "/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {
    private final EventFacade eventFacade;


    @GetMapping
    @Operation(summary = "Get all events",
            description = "Retrieves a list of all events available in the system.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all events",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EventDTO.class)))
    public ResponseEntity<List<EventDTO>> getAll(){
        log.info("GET request received to retrieve all events.");
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
        log.info("GET request received to retrieve event with ID: {}", eventId);
        EventDTO result = eventFacade.getById(eventId);
        if (result == null) {
            log.info("Event with ID {} not found.", eventId);
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
            log.info("POST request received to create a new event.");
            EventDTO result = eventFacade.create(newEvent);
            if (result == null){
                log.info("Failed to create event.");
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
    public ResponseEntity<?> update(
            @Parameter(description = "ID of the event to update", required = true)
            @PathVariable Long eventId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated event data", required = true)
            @RequestBody EventDTO updatedEvent){
        log.info("PUT request received to update event with ID: {}", eventId);
        try {
            EventDTO result = eventFacade.update(eventId, updatedEvent);
            return ResponseEntity.ok(result);
        } catch (EntityNotFoundException e) {
            log.warn("Failed to update event with ID {}: {}", eventId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.warn("Failed to update event with ID {}: {}", eventId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalAccessException e) {
            log.warn("Failed to update event with ID {}: {}", eventId, e.getMessage());
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
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
        log.info("GET request received to retrieve events for user with ID: {}", userId);
        List<EventDTO> result = eventFacade.getByUserId(userId);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/{eventId}/purchase")
    @Operation(summary = "Purchase ticket for an event",
            description = "Processes a ticket purchase for the specified event.")
    @ApiResponse(responseCode = "200", description = "Ticket purchased successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TicketResponse.class)))
    @ApiResponse(responseCode = "400", description = "Purchase failed due to invalid data or other issues")
    public ResponseEntity<?> purchase(
            @Parameter(description = "ID of the event to purchase tickets for", required = true)
            @PathVariable Long eventId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Purchase request details", required = true)
            @RequestBody PurchaseRequest request) {
        log.info("POST request received to purchase tickets for event with ID: {}", eventId);
        try {
            List<TicketResponse> result = eventFacade.purchaseTicket(eventId, request);
            return ResponseEntity.ok(result);
        } catch (EntityNotFoundException e) {
            log.warn("Event or user not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            log.warn("Error purchasing tickets: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "Delete an event",
            description = "Deletes the event with the specified ID.")
    @ApiResponse(responseCode = "204", description = "Event deleted successfully")
    @ApiResponse(responseCode = "404", description = "Event not found")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID of the event to be deleted", required = true)
            @PathVariable Long eventId) {
        log.info("DELETE request received to delete event with ID: {}", eventId);
        try {
            if(!eventFacade.delete(eventId)) {
                log.info("Event with ID {} not found for delete.", eventId);
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
        } catch (IllegalAccessException e){
            log.warn("Failed to delete event with ID {}: {}", eventId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/like")
    @Operation(summary = "Like an event",
            description = "Marks the event as liked by the user.")
    @ApiResponse(responseCode = "200", description = "Event liked successfully")
    @ApiResponse(responseCode = "404", description = "Event or user not found")
    public ResponseEntity<?> like(@RequestBody LikeRequest request) {
        log.info("PUT request received to like event with ID {} by user with ID {}.", request.getEventId(), request.getUserId());
        if (!eventFacade.like(request.getEventId(), request.getUserId())) {
            log.info("Failed to like event with ID {} by user with ID {}.", request.getEventId(), request.getUserId());
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
        log.info("PUT request received to unlike event with ID {} by user with ID {}.", request.getEventId(), request.getUserId());
        if (!eventFacade.unlike(request.getEventId(), request.getUserId())) {
            log.info("Failed to unlike event with ID {} by user with ID {}.", request.getEventId(), request.getUserId());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}
