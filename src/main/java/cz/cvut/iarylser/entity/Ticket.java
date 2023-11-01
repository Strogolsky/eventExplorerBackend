package cz.cvut.iarylser.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "seat")
    private int seat;
    @Column(name = "event_id")
    private Long eventId;
    @Column(name = "customer_nickname")
    private String customerNickname;
    @Column(name = "organizer_nickname")
    private String organizerNickname;
    @Column(name = "details")
    private String details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getCustomerNickname() {
        return customerNickname;
    }

    public void setCustomerNickname(String customerNickname) {
        this.customerNickname = customerNickname;
    }

    public String getOrganizerNickname() {
        return organizerNickname;
    }

    public void setOrganizerNickname(String organizerNickname) {
        this.organizerNickname = organizerNickname;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
