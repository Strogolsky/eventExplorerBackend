package cz.cvut.iarylser.dao.repository;

import cz.cvut.iarylser.dao.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    public List<Event> findAllByCapacity(Integer capacity);
}
