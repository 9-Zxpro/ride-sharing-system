package me.jibajo.captain_service.repositories;

import me.jibajo.captain_service.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
