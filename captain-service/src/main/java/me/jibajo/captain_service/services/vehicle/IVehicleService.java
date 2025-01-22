package me.jibajo.captain_service.services.vehicle;

import me.jibajo.captain_service.dto.VehicleDTO;
import me.jibajo.captain_service.entities.Vehicle;

public interface IVehicleService {
    VehicleDTO getVehicleByCaptainId(Long captainId);
    Vehicle addOrUpdateVehicle(Long captainId, VehicleDTO vehicleDTO);
    VehicleDTO convertToDto(Vehicle vehicle);
}
