package cz.cvut.iarylser.facade;

import cz.cvut.iarylser.dao.DTO.EventDTO;
import cz.cvut.iarylser.dao.DTO.PurchaseRequest;
import cz.cvut.iarylser.dao.DTO.TicketDTO;

import java.util.List;

public interface EventFacade extends CrudFacade<EventDTO, Long> {
    public List<TicketDTO> purchaseTicket(Long eventId, PurchaseRequest request);
    public List<EventDTO> getByUserId(Long id);
    public boolean like(Long eventId, Long userId);
    public boolean unlike(Long eventId, Long userId);
}
