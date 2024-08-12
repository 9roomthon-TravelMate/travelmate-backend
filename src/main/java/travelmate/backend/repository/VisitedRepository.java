package travelmate.backend.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import travelmate.backend.entity.Visited;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitedRepository extends JpaRepository<Visited, Long> {

    @Query("SELECT v.contentId FROM Visited v WHERE v.traveler.id = :travelerId")
    List<String> findContentIdsByTravelerId(@Param("travelerId") Long travelerId);
}