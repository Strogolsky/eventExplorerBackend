package cz.cvut.iarylser.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nickname", unique = true)
    private String nickname;
    @Column(name = "password")
    private String password;
    @Column(name = "age")
    private int age;
    @Column(name = "email")
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Ticket> tickets = new ArrayList<>();
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Event> createdEvents = new ArrayList<>();
    @ManyToMany(mappedBy = "likeBy", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Event> likeByMe = new HashSet<>();

    public void likeEvent(Event event) {
        this.likeByMe.add(event);
    }

    public void unlikeEvent(Event event) {
        this.likeByMe.remove(event);
    }
}
