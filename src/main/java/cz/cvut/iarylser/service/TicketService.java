package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.entity.Ticket;
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
}
