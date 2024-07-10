package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.entity.*;
import cz.cvut.iarylser.dao.mappersDTO.TicketMapperDTO;
import cz.cvut.iarylser.dao.repository.TicketRepository;
import cz.cvut.iarylser.dao.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static cz.cvut.iarylser.dao.entity.TicketStatus.INVALID;

@Service
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    public TicketService(TicketRepository ticketRepository, UserRepository userRepository){
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }
    public Ticket getById(Long ticketId){
        return ticketRepository.findById(ticketId).orElse(null);
    }
    public List<Ticket> getByUser(Long userId){
        return ticketRepository.findByIdCustomer(userId);
    }

    public Ticket create(Event event, User customer){
        Ticket ticket = new Ticket();

        ticket.setEventId(event.getId());
        ticket.setIdCustomer(customer.getId());
        ticket.setIdOrganizer(event.getOrganizerId());

        setDetailsFromEvent(event,ticket);
        ticket.setTicketStatus(TicketStatus.ACTIVE);

        ticket.setUser(customer);
        ticket.setEvent(event);

        customer.getTickets().add(ticket);
        event.getTickets().add(ticket);

        userRepository.save(customer);
        return ticketRepository.save(ticket);
    }

    public Ticket update(Long ticketId, Ticket updatedTicket) {
        Ticket existingTicket = ticketRepository.findById(ticketId).orElse(null);
        if (existingTicket == null) {
            log.warn("Ticket with id {} not found for update", ticketId);
            return null;
        }
        existingTicket.setDetails(updatedTicket.getDetails());
        return ticketRepository.save(existingTicket);
    }
    public void setDetailsFromEvent(Event event, Ticket ticket) {
        String details = "Event: " + event.getTitle() +
                ", Date: " + event.getDateAndTime().toString() +
                ", Location: " + event.getLocation();
        ticket.setDetails(details);
    }

    public boolean delete(Long ticketId){
        if(!ticketRepository.existsById(ticketId)){
            return false;
        }
        ticketRepository.deleteById(ticketId);
        return true;
    }
}
