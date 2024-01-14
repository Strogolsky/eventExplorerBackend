package cz.cvut.iarylser.dao.repository;

import cz.cvut.iarylser.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByNickname( String nickname);

    public boolean existsByNickname(String nickname);

}
