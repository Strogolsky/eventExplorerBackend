package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.entity.*;
import cz.cvut.iarylser.dao.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static cz.cvut.iarylser.dao.entity.TicketStatus.INVALID;

@Service
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;
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
    public TicketDTO convertToDto(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setEventId(ticket.getEventId());
        ticketDTO.setIdCustomer(ticket.getIdCustomer());
        ticketDTO.setIdOrganizer(ticket.getIdOrganizer());
        ticketDTO.setDetails(ticket.getDetails());
        ticketDTO.setTicketStatus(ticket.getTicketStatus());


        return ticketDTO;
    }
    public List<TicketDTO> convertTicketsToDTOs(List<Ticket> tickets) {
        if (tickets == null) {
            return new ArrayList<>();
        }

        List<TicketDTO> ticketDTOs = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketDTOs.add(convertToDto(ticket));
        }
        return ticketDTOs;
    }
    public void deactivateTickets(Event event) {
        List<Ticket> tickets = event.getTickets();
        for (Ticket ticket : tickets) {
            ticket.setTicketStatus(INVALID);
            ticketRepository.save(ticket);
        }
    }

    public Ticket updateTicket(Long ticketId, Ticket updatedTicket) {
        Ticket existingTicket = ticketRepository.findById(ticketId).orElse(null);
        if (existingTicket == null) {
            log.warn("Ticket with id {} not found for update", ticketId);
            return null;
        }
        existingTicket.setDetails(updatedTicket.getDetails());
        return ticketRepository.save(existingTicket);
    }

    public void deleteTicket(Long ticketId){
        ticketRepository.deleteById(ticketId);
    }
}
