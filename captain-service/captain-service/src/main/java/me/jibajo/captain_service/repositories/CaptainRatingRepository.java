package me.jibajo.captain_service.repositories;

import me.jibajo.captain_service.entities.CaptainRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaptainRatingRepository extends JpaRepository<CaptainRating, Long> {
    List<CaptainRating> findByCaptain_CaptainId(Long captainId);
}