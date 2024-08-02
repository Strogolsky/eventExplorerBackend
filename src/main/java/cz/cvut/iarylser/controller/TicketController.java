package cz.cvut.iarylser.controller;

import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.entity.Ticket;
import cz.cvut.iarylser.dao.mappersDTO.TicketMapperDTO;
import cz.cvut.iarylser.facade.TicketFacade;
import cz.cvut.iarylser.facade.TicketFacadeImpl;
import cz.cvut.iarylser.service.TicketServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/tickets")
@CrossOrigin("*")
@Slf4j
public class TicketController {
    private final TicketFacade ticketFacade;
    public TicketController(TicketFacadeImpl ticketFacade){
        this.ticketFacade = ticketFacade;
    }
    @GetMapping("/{ticketId}")
    @Operation(summary = "Get Ticket by ID",
            description = "Retrieves a ticket by its unique identifier. If the ticket is not found, returns a 404 status.")
    @ApiResponse(responseCode = "200", description = "Ticket found and returned",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = TicketDTO.class)))
    @ApiResponse(responseCode = "404", description = "Ticket not found for the provided ID")
    public ResponseEntity<TicketDTO> getById(
            @Parameter(description = "Unique identifier of the ticket to be retrieved", required = true)
            @PathVariable Long ticketId) {
        log.info("GET request received to retrieve ticket with ID: {}", ticketId);
        TicketDTO result = ticketFacade.getById(ticketId);
        if (result == null) {
            log.warn("Ticket with ID {} not found", ticketId);
            return ResponseEntity.notFound().build();
        }
        log.info("Ticket with ID {} found and returned", ticketId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get Tickets by User",
            description = "Retrieves all tickets associated with a user by the user's ID.")
    @ApiResponse(responseCode = "200", description = "Tickets for the user found and returned",
            content = @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = TicketDTO.class))))
    public ResponseEntity<List<TicketDTO>> getByUser(@PathVariable Long userId) {
        log.info("GET request received to retrieve tickets for user with ID: {}", userId);
        List<TicketDTO> result = ticketFacade.getByUserId(userId);
        log.info("Tickets for user with ID {} found and returned", userId);
        return ResponseEntity.ok(result);
    }
}
