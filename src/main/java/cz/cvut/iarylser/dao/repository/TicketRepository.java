package cz.cvut.iarylser.dao.repository;

import cz.cvut.iarylser.dao.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    public List<Ticket> findByIdCustomer(Long userId);
}
