package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.entity.*;
import cz.cvut.iarylser.dao.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static cz.cvut.iarylser.dao.entity.TicketStatus.INVALID;

@Service
@Slf4j
public class TicketService {
    private TicketRepository ticketRepository;
    public TicketService(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }
    public TicketDTO getTicketById(Long ticketId){
        return convertToDto(ticketRepository.findById(ticketId).orElse(null));
    }
    public List<TicketDTO> getTicketByUser(Long userId){
        return convertTicketsToDTOs(ticketRepository.findByIdCustomer(userId));
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
}
