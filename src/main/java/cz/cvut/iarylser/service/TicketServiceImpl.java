package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.entity.*;
import cz.cvut.iarylser.dao.repository.TicketRepository;
import cz.cvut.iarylser.dao.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TicketServiceImpl implements TicketService{
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    public TicketServiceImpl(TicketRepository ticketRepository, UserRepository userRepository){
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }
    @Override
    @Cacheable(value = "ticket", key = "#ticketId")
    public Ticket getById(Long ticketId){
        log.info("Fetching ticket with id: {}", ticketId);
        return ticketRepository.findById(ticketId).orElse(null);
    }
    @Override
    @Cacheable(value = "ticket", key = "#userId")
    public List<Ticket> getByUser(Long userId){
        log.info("Fetching tickets for user with id: {}", userId);
        return ticketRepository.findByCustomerId(userId);
    }
    @Override
    public Ticket create(Event event, User customer){
        log.info("Creating ticket for event: {} and customer: {}", event, customer);
        Ticket ticket = new Ticket();

        ticket.setEventId(event.getId());
        ticket.setCustomerId(customer.getId());
        ticket.setOrganizerId(event.getOrganizerId());

        setDetailsFromEvent(event,ticket);
        ticket.setTicketStatus(TicketStatus.ACTIVE);

        ticket.setUser(customer);
        ticket.setEvent(event);

        customer.getTickets().add(ticket);
        event.getTickets().add(ticket);

        userRepository.save(customer);
        return ticketRepository.save(ticket);
    }
    @Override
    @CachePut(value = "ticket", key = "#ticketId")
    public Ticket update(Long ticketId, Ticket updatedTicket) {
        log.info("Updating ticket with id: {}", ticketId);
        Ticket existingTicket = ticketRepository.findById(ticketId).orElse(null);
        if (existingTicket == null) {
            log.warn("Ticket with id {} not found for update", ticketId);
            return null;
        }
        existingTicket.setDetails(updatedTicket.getDetails());
        return ticketRepository.save(existingTicket);
    }
    @Override
    public void setDetailsFromEvent(Event event, Ticket ticket) { // todo private?
        String details = "Event: " + event.getTitle() +
                ", Date: " + event.getDateAndTime().toString() +
                ", Location: " + event.getLocation();
        ticket.setDetails(details);
    }
    @Override
    @CacheEvict(value = "ticket", key = "#ticketId")
    public boolean delete(Long ticketId){
        log.info("Deleting ticket with id: {}", ticketId);
        if(!ticketRepository.existsById(ticketId)){
            log.warn("Ticket with id {} not found for deletion", ticketId);
            return false;
        }
        ticketRepository.deleteById(ticketId); // todo change free places
        return true;
    }
}
