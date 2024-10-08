package cz.cvut.iarylser.dao.dto;

import cz.cvut.iarylser.dao.entity.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private Long eventId;
    private Long customerId;
    private Long organizerId;
    private String details;
    private TicketStatus ticketStatus;
}
