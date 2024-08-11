package travelmate.backend.repository;

import travelmate.backend.entity.Member;
import travelmate.backend.entity.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Integer> {

    Optional<Preference> findByTraveler(Member traveler);
}
