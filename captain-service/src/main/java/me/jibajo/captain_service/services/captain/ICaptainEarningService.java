package me.jibajo.captain_service.services.captain;

import me.jibajo.captain_service.entities.CaptainEarnings;

import java.time.LocalDate;
import java.util.List;

public interface ICaptainEarningService {
     List<CaptainEarnings> getEarningsByCaptain(Long captainId);

     List<CaptainEarnings> getEarningsByCaptainAndDateRange(Long captainId, LocalDate startDate, LocalDate endDate);

     CaptainEarnings addEarnings(CaptainEarnings earnings);
}
