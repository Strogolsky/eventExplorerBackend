package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.entity.*;
import cz.cvut.iarylser.dao.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TicketService {
    private TicketRepository ticketRepository;
    public TicketService(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }
    public Ticket getTicketById(Long ticketId){
        return ticketRepository.findById(ticketId).orElse(null);
    }
    public List<Ticket> getTicketByUser(Long userId){
        return ticketRepository.findByIdCustomer(userId);
    }

    public Ticket createTicket(Event event, User customer){
        Ticket ticket = new Ticket();
        ticket.setEventId(event.getId());
        ticket.setIdCustomer(customer.getId());
        ticket.setIdOrganizer(event.getIdOrganizer());
        ticket.setDetails(event.getDescription()); // todo change
        ticket.setTicketStatus(TicketStatus.ACTIVE);
        ticket.setUser(customer);
        ticket.setEvent(event);
        return ticketRepository.save(ticket);
    }
}
