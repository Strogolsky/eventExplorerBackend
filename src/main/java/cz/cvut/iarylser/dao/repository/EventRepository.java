package cz.cvut.iarylser.dao.repository;

import cz.cvut.iarylser.dao.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    public List<Event> findByIdOrganizer(Long userId);

    @Query(value = "SELECT e FROM Event e WHERE size(e.likeBy) > :likes")
    Collection<Event> findByLikedGreaterThan(int likes);

}
