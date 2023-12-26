package cz.cvut.iarylser.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    // todo create password
    @Id
    @Column(name = "nickname")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String nickname;
    @OneToMany(mappedBy = "user")
    private Set<Ticket> tickets = new HashSet<>();
    @OneToMany(mappedBy = "user")
    private Set<Event> createdEvents = new HashSet<>();
    @ManyToMany(mappedBy = "likeBy")
    private Set<Event> likeByMe;
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

    public User(String nickname, int age, String email) {
        this.nickname = nickname;
        this.age = age;
        this.email = email;
    }
}
