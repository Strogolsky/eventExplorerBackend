package cz.cvut.iarylser.dao.repository;

import cz.cvut.iarylser.dao.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    public List<Event> findAllByCapacity(Integer capacity);

}
