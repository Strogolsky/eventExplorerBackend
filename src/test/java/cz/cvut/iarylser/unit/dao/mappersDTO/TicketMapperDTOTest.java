package cz.cvut.iarylser.unit.dao.mappersDTO;

import cz.cvut.iarylser.dao.DTO.TicketDTO;
import cz.cvut.iarylser.dao.entity.Ticket;
import cz.cvut.iarylser.dao.entity.TicketStatus;
import cz.cvut.iarylser.dao.mappersDTO.TicketMapperDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicketMapperDTOTest {
    private TicketMapperDTO ticketMapperDTO;

    private Ticket ticket;
    private TicketDTO dto;
    private List<TicketDTO> dtoList;
    private List<Ticket> ticketList;


    @BeforeEach
    void setUp() {
        ticketMapperDTO = new TicketMapperDTO();
        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setEventId(2L);
        ticket.setCustomerId(3L);
        ticket.setOrganizerId(4L);
        ticket.setDetails("details");
        ticket.setTicketStatus(TicketStatus.ACTIVE);

        dto = new TicketDTO();
        dto.setId(1L);
        dto.setEventId(2L);
        dto.setCustomerId(3L);
        dto.setOrganizerId(4L);
        dto.setDetails("details");
        dto.setTicketStatus(TicketStatus.ACTIVE);

        ticketList = new ArrayList<>();
        dtoList = new ArrayList<>();

        ticketList.add(ticket);
        ticketList.add(ticket);

        dtoList.add(dto);
        dtoList.add(dto);

    }
    @Test
    void toDTO() {
        TicketDTO newDto = ticketMapperDTO.toDTO(ticket);
        assertEquals(dto, newDto);

    }

    @Test
    void toDTOList() {
        List<TicketDTO> newDtoList = ticketMapperDTO.toDTOList(ticketList);
        assertEquals(dtoList, newDtoList);
    }

    @Test
    void toEntity() {
        Ticket newTicket = ticketMapperDTO.toEntity(dto);
        assertEquals(ticket, newTicket);
    }
}