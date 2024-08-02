package cz.cvut.iarylser.facade;

import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.entity.Event;
import cz.cvut.iarylser.dao.entity.Ticket;
import cz.cvut.iarylser.dao.entity.User;

import java.util.List;

public interface TicketFacade {
    public TicketDTO getById(Long id);
    public List<TicketDTO> getByUserId(Long id);
}
