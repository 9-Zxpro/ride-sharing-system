package me.jibajo.captain_service.services.captain;

import me.jibajo.captain_service.entities.CaptainEarnings;
import me.jibajo.captain_service.repositories.CaptainEarningsRepository;

import java.time.LocalDate;
import java.util.List;

public class CaptainEarningServiceImp implements ICaptainEarningService {
    private final CaptainEarningsRepository earningsRepository;

    public CaptainEarningServiceImp(CaptainEarningsRepository earningsRepository) {
        this.earningsRepository = earningsRepository;
    }

    public List<CaptainEarnings> getEarningsByCaptain(Long captainId) {
        return earningsRepository.findByCaptain_CaptainId(captainId);
    }

    public List<CaptainEarnings> getEarningsByCaptainAndDateRange(Long captainId, LocalDate startDate, LocalDate endDate) {
        return earningsRepository.findByCaptain_CaptainIdAndDateBetween(captainId, startDate, endDate);
    }

    public CaptainEarnings addEarnings(CaptainEarnings earnings) {
        return earningsRepository.save(earnings);
    }
}
