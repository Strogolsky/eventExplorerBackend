package cz.cvut.iarylser.dao.repository;

import cz.cvut.iarylser.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
