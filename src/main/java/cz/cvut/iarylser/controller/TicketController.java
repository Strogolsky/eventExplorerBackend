package cz.cvut.iarylser.controller;

import ch.qos.logback.classic.Logger;
import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.entity.Ticket;
import cz.cvut.iarylser.service.TicketService;
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
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long ticketId){
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        TicketDTO ticketDTO = ticketService.convertToDto(ticket);
        return ResponseEntity.ok(ticketDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketDTO>> getTicketByUser(@PathVariable Long userId){
        List<TicketDTO> result = ticketService.convertTicketsToDTOs(ticketService.getTicketByUser(userId));
        return ResponseEntity.ok(result);
    }
}
