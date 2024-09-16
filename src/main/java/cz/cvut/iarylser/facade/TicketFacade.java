package cz.cvut.iarylser.facade;

import cz.cvut.iarylser.dao.dto.TicketResponse;

import java.util.List;

public interface TicketFacade {
    public TicketResponse getById(Long id);
    public List<TicketResponse> getByUserId(Long id);
}
