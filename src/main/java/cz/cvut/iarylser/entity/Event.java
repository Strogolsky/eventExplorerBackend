package cz.cvut.iarylser.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "event")
public class Event {
    private enum Topics{
        SPORT,
        GAME,
        TRAVEL,
        PARTY,
        EDUCATION,
        CULTURE,
        FASHION,
        FAMILY,
        CHARITY

    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "organizer_nickname")
    private String organizerNickname;
    @Column(name = "title")
    private String title;
    @Column(name = "date_and_time")
    private LocalDateTime dateAndTime; // todo !!!
    @Column(name = "ticket_price")
    private int ticketPrice;
    @Column(name = "location")
    private String location;
    @Column(name = "capacity")
    private int capacity;
    @Column(name = "description")
    private String description;
    @Column(name = "topic")
    private Topics topic;
    @Column(name = "age_restriction")
    private boolean ageRestriction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizerNickname() {
        return organizerNickname;
    }

    public void setOrganizerNickname(String organizerNickname) {
        this.organizerNickname = organizerNickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Topics getTopic() {
        return topic;
    }

    public void setTopic(Topics topic) {
        this.topic = topic;
    }

    public boolean isAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(boolean ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

}
