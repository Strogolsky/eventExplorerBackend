package cz.cvut.iarylser.controller;

import ch.qos.logback.classic.Logger;
import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.entity.Ticket;
import cz.cvut.iarylser.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/ticket")
@CrossOrigin("*")
public class TicketController {
    private final TicketService ticketService;
    public TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }
    @GetMapping("/{ticketId}")
    @Operation(summary = "Get Ticket by ID",
            description = "Retrieves a ticket by its unique identifier. If the ticket is not found, returns a 404 status.")
    @ApiResponse(responseCode = "200", description = "Ticket found and returned",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TicketDTO.class)))
    @ApiResponse(responseCode = "404", description = "Ticket not found for the provided ID")
    public ResponseEntity<TicketDTO> getTicketById(
            @Parameter(description = "Unique identifier of the ticket to be retrieved", required = true)
            @PathVariable Long ticketId){
        Ticket ticket = ticketService.getById(ticketId);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        TicketDTO ticketDTO = ticketService.convertToDto(ticket);
        return ResponseEntity.ok(ticketDTO);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get Tickets by User",
            description = "Retrieves all tickets associated with a user by the user's ID.")
    @ApiResponse(responseCode = "200", description = "Tickets for the user found and returned",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TicketDTO.class))))
    public ResponseEntity<List<TicketDTO>> getTicketByUser(@PathVariable Long userId){
        List<TicketDTO> result = ticketService.convertTicketsToDTOs(ticketService.getByUser(userId));
        return ResponseEntity.ok(result);
    }
}
