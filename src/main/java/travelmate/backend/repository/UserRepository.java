package travelmate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import travelmate.backend.entity.Member;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {

    Member findByUsername (String username);
}
