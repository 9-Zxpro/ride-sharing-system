package me.jibajo.captain_service.services.vehicle;

import lombok.RequiredArgsConstructor;
import me.jibajo.captain_service.dto.VehicleDTO;
import me.jibajo.captain_service.entities.Captain;
import me.jibajo.captain_service.entities.Vehicle;
import me.jibajo.captain_service.exceptions.NotFoundException;
import me.jibajo.captain_service.repositories.CaptainRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements IVehicleService {

    private final CaptainRepository captainRepository;
    private final ModelMapper modelMapper;

    @Override
    public VehicleDTO getVehicleByCaptainId(Long captainId) {
        Captain captain = captainRepository.findById(captainId)
                .orElseThrow(() -> new NotFoundException("Captain not found with id: " + captainId));
        return convertToDto(captain.getVehicle());
    }

    @Override
    public Vehicle addOrUpdateVehicle(Long captainId, VehicleDTO vehicleDTO) {
        Captain captain = captainRepository.findById(captainId)
                .orElseThrow(() -> new NotFoundException("Captain not found with id: " + captainId));

        captain.setVehicle( mapVehicleDTOToVehicle(vehicleDTO));
        Captain updatedCaptain = captainRepository.save(captain);
        return updatedCaptain.getVehicle();
    }

    @Override
    public VehicleDTO convertToDto(Vehicle vehicle) {
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    private Vehicle mapVehicleDTOToVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setRegistrationNumber(vehicleDTO.getRegistrationNumber());
        vehicle.setRegistrationUrl(vehicleDTO.getRegistrationUrl());
        return vehicle;
    }
}

