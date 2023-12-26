package cz.cvut.iarylser.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // todo create password
    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "nickname", unique = true)
    private String nickname; // todo
    @Column(name = "age")
    private int age;
    @Column(name = "email")
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "description", columnDefinition = "text") // TODO check
    private String description;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Ticket> tickets = new HashSet<>();
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Event> createdEvents = new HashSet<>();
    @ManyToMany(mappedBy = "likeBy")
    @JsonIgnore
    private Set<Event> likeByMe = new HashSet<>();

    public User(String nickname, int age, String email, String firstName, String lastName, String description) {
        this.nickname = nickname;
        this.age = age;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
    }



}
