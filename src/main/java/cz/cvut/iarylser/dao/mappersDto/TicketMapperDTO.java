package cz.cvut.iarylser.dao.mappersDto;

import cz.cvut.iarylser.dao.dto.TicketDTO;
import cz.cvut.iarylser.dao.entity.Ticket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TicketMapperDTO implements MapperDTO<TicketDTO, Ticket> {
    @Override
    public TicketDTO toDTO(Ticket entity) {
        if (entity == null) {
            log.warn("Attempted to convert null Ticket entity to DTO");
            return null;
        }

        TicketDTO dto = new TicketDTO();
        dto.setId(entity.getId());
        dto.setEventId(entity.getEventId());
        dto.setCustomerId(entity.getCustomerId());
        dto.setOrganizerId(entity.getOrganizerId());
        dto.setDetails(entity.getDetails());
        dto.setTicketStatus(entity.getTicketStatus());

        log.info("Converted Ticket entity to DTO: {}", dto);
        return dto;
    }

    @Override
    public List<TicketDTO> toDTOList(List<Ticket> entities) {
        if (entities == null) {
            log.warn("Attempted to convert null list of Ticket entities to DTO list");
            return new ArrayList<>();
        }

        log.info("Converting list of Ticket entities to DTOs, size: {}", entities.size());
        List<TicketDTO> listDTO = new ArrayList<>();
        for (Ticket ticket : entities) {
            listDTO.add(toDTO(ticket));
        }
        log.info("Converted {} Ticket entities to DTOs", listDTO.size());
        return listDTO;
    }

    @Override
    public Ticket toEntity(TicketDTO dto) {
        if (dto == null) {
            log.warn("Attempted to convert null TicketDTO to entity");
            return null;
        }
        Ticket entity = new Ticket();
        entity.setId(dto.getId());
        entity.setEventId(dto.getEventId());
        entity.setCustomerId(dto.getCustomerId());
        entity.setOrganizerId(dto.getOrganizerId());
        entity.setDetails(dto.getDetails());
        entity.setTicketStatus(dto.getTicketStatus());
        log.info("Converted TicketDTO to entity: {}", entity);
        return entity;
    }
}
