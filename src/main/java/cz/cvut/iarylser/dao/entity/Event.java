package cz.cvut.iarylser.dao.entity;

import ch.qos.logback.classic.Logger;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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
    private LocalDateTime dateAndTime;
    @Column(name = "ticket_price")
    private int ticketPrice;
    @Column(name = "location")
    private String location;
    @Column(name = "capacity")
    private int capacity;
    @Column(name = "sold_tickets")
    private int soldTickets;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "topic")
    private Topics topic;
    @Column(name = "age_restriction")
    private boolean ageRestriction;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_likes",
            joinColumns = @JoinColumn(name = "id_event"),
            inverseJoinColumns = @JoinColumn(name = "id_user")
    )
    private List<User> likeBy = new ArrayList<>();
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();
    private Logger log;

    public int getAvailableSeat(){
        return capacity - soldTickets;
    }
    public boolean updateCapacity(int newCapacity) {
        if (soldTickets <= newCapacity) {
            this.capacity = newCapacity;
            return true;
        }
        return false;
    }
}
