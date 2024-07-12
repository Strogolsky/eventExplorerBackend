package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.Ticket;
import cz.cvut.iarylser.dao.entity.User;

import java.util.List;

public interface TicketService {
    public Ticket getById(Long ticketId);
    public List<Ticket> getByUser(Long userId);
    public Ticket create(Event event, User customer);
    public Ticket update(Long ticketId, Ticket updatedTicket);
    public void setDetailsFromEvent(Event event, Ticket ticket);
    public boolean delete(Long ticketId);
}
