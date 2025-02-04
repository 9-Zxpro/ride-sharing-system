package me.jibajo.admin_service.services;

import me.jibajo.admin_service.entities.SystemStatistics;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {
    public SystemStatistics getStatistics() {
        SystemStatistics stats = new SystemStatistics();
        return stats;
    }
}
