package cz.cvut.iarylser.dao.repository;

import cz.cvut.iarylser.dao.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
