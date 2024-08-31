package cz.cvut.iarylser.service;

import cz.cvut.iarylser.dao.DTO.PurchaseRequest;
import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.User;

import java.util.List;

public interface EventService extends CrudService<Event,Long>{
    public List<TicketDTO> purchaseTicket(Long eventId, PurchaseRequest request);
    public List<Event> getByUserId(Long userId);
    public void updateForOrgChange(Event event, User organizer);
    public boolean like(Long eventId, Long userId);
    public boolean unlike(Long eventId, Long userId);
}
