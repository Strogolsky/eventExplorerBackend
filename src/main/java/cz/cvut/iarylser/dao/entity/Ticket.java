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
    private Long customerId;
    @Column(name = "id_organizer")
    private Long organizerId;
    @Column(name = "details", columnDefinition = "text")
    private String details;
    @Column(name = "ticket_status")
    private TicketStatus ticketStatus;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_event")
    private Event event;
}
