package me.jibajo.captain_service.repositories;

import me.jibajo.captain_service.entities.CaptainEarnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CaptainEarningsRepository extends JpaRepository<CaptainEarnings, Long> {
    List<CaptainEarnings> findByCaptain_CaptainId(Long captainId);
    List<CaptainEarnings> findByCaptain_CaptainIdAndDateBetween(Long captainId, LocalDate startDate, LocalDate endDate);
}
