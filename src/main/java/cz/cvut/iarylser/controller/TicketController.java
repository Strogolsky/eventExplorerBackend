package cz.cvut.iarylser.controller;

import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.Ticket;
import cz.cvut.iarylser.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    private TicketService ticketService;
    public TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }
    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long ticketId){
        Ticket ticket = ticketService.getTicketById(ticketId);
        // todo check on null
        return ResponseEntity.ok(ticket);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getTicketByUser(@PathVariable Long userId){
        List<Ticket> result = ticketService.getTicketByUser(userId);
        return ResponseEntity.ok(result);
    }

    // todo getByUser
}
