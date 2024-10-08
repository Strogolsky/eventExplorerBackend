package cz.cvut.iarylser.dao.dto;

import cz.cvut.iarylser.dao.entity.Topics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long id;
    private String title;
    private LocalDateTime dateAndTime;
    private int likes;
    private int ticketPrice;
    private String location;
    private int capacity;
    private String organizer;
    private int soldTickets;
    private String description;
    private Topics topic;
    private boolean ageRestriction;
}
