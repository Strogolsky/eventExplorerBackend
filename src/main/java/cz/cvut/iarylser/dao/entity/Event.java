package cz.cvut.iarylser.dao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "public", name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_event")
    private Long id;
    @Column(name = "id_organizer")
    private Long idOrganizer;
    @Column(name = "title")
    private String title;
    @Column(name = "organizer")
    private String organizer;
    @Column(name = "date_and_time")
    private LocalDateTime dateAndTime; // todo !!!
    @Column(name = "ticket_price")
    private int ticketPrice;
    @Column(name = "location")
    private String location;
    @Column(name = "capacity")
    private int capacity;
    @Column(name = "description", columnDefinition = "text") // todo check
    private String description;
    @Column(name = "topic")
    private Topics topic;
    @Column(name = "age_restriction")
    private boolean ageRestriction;
    @ManyToOne
//    @JoinColumn(name = "organizer_nickname", referencedColumnName = "nickname")
    private User user;
    @ManyToMany
    private Set<User> likeBy = new HashSet<>();
    @OneToMany(mappedBy = "event")
    private Set<Ticket> tickets = new HashSet<>();
}
