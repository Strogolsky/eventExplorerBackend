package cz.cvut.iarylser.dao.mappersDTO;

import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.entity.Ticket;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TicketMapperDTO implements MapperDTO<TicketDTO, Ticket> {
    @Override
    public TicketDTO toDTO(Ticket entity) {
        if (entity == null) {
            return null;
        }

        TicketDTO dto = new TicketDTO();
        dto.setId(entity.getId());
        dto.setEventId(entity.getEventId());
        dto.setCustomerId(entity.getCustomerId());
        dto.setOrganizerId(entity.getOrganizerId());
        dto.setDetails(entity.getDetails());
        dto.setTicketStatus(entity.getTicketStatus());


        return dto;
    }

    @Override
    public List<TicketDTO> toDTOList(List<Ticket> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }

        List<TicketDTO> listDTO = new ArrayList<>();
        for (Ticket ticket : entities) {
            listDTO.add(toDTO(ticket));
        }
        return listDTO;
    }
}
