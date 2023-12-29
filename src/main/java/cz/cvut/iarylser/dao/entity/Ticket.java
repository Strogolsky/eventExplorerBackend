package cz.cvut.iarylser.dao.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(schema = "public",name = "ticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_ticket")
    private Long id;
    @Column(name = "event_id")
    private Long eventId;
    @Column(name = "id_customer")
    private Long idCustomer;
    @Column(name = "id_organizer")
    private Long idOrganizer;
    @Column(name = "details", columnDefinition = "text") // TODO check
    private String details;
    @Column(name = "ticket_status")
    private TicketStatus ticketStatus;

    @ManyToOne
//    @JoinColumn(name = "customer_nickname", referencedColumnName = "nickname")
    private User user;

    @ManyToOne
//    @JoinColumn(name = "event_id", referencedColumnName = "id_event" )
    private Event event;
}
