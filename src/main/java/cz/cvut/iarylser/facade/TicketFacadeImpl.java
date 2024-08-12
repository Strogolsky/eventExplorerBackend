package cz.cvut.iarylser.facade;

import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.entity.Ticket;
import cz.cvut.iarylser.dao.mappersDTO.TicketMapperDTO;
import cz.cvut.iarylser.service.TicketService;
import cz.cvut.iarylser.service.TicketServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@RequiredArgsConstructor
public class TicketFacadeImpl implements TicketFacade {
    private final TicketMapperDTO ticketMapper;
    private final TicketService ticketService;

    @Override
    public TicketDTO getById(Long id) {
        Ticket result = ticketService.getById(id);
        return ticketMapper.toDTO(result);
    }

    @Override
    public List<TicketDTO> getByUserId(Long id) {
        List<Ticket> result = ticketService.getByUser(id);
        return ticketMapper.toDTOList(result);
    }
}
