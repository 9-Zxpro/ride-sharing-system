package me.jibajo.admin_service.controller;

import lombok.RequiredArgsConstructor;
import me.jibajo.admin_service.services.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<?> getStatistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }
}
