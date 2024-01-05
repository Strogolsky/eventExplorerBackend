package cz.cvut.iarylser.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Long id;
    private Long eventId;
    private Long idCustomer;
    private Long idOrganizer;
    private String details;
    private TicketStatus ticketStatus;
}
