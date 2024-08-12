package travelmate.backend.repository;


import travelmate.backend.entity.Visited;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitedRepository extends JpaRepository<Visited, Long> {
}