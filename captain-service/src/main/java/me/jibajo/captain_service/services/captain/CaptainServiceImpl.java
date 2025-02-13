package me.jibajo.captain_service.services.captain;

import lombok.RequiredArgsConstructor;
import me.jibajo.captain_service.config.RabbitConfig;
import me.jibajo.captain_service.dto.APIResponse;
import me.jibajo.captain_service.dto.CaptainDTO;
import me.jibajo.captain_service.entities.Captain;
import me.jibajo.captain_service.entities.Vehicle;
import me.jibajo.captain_service.exceptions.AlreadyExistsException;
import me.jibajo.captain_service.exceptions.ResourceNotFoundException;
import me.jibajo.captain_service.repositories.CaptainRepository;
import me.jibajo.captain_service.repositories.VehicleRepository;
import me.jibajo.captain_service.dto.CaptainRegRequest;
import me.jibajo.captain_service.services.feignclient.RideManagerClient;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CaptainServiceImpl implements ICaptainService {

    private final CaptainRepository captainRepository;
    private final VehicleRepository vehicleRepository;
    private final ModelMapper modelMapper;
    private final AmqpAdmin amqpAdmin;
    private final RabbitConfig rabbitConfig;
    private final RideManagerClient rideManagerClient;

    @Value("${ride-offer.exchange}")
    private String rideOfferExchange;

    @Value("${ride-offer.queues-prefix}")
    private String rideOfferQueuePrefix;

    @Value("${ride-offer.routing-key-prefix}")
    private String rideOfferRoutingKeyPrefix;


    @Override
    public Captain createCaptain(CaptainRegRequest captainRegRequest) {
        if (captainRepository.existsByPhone(captainRegRequest.getPhone())) {
            throw new AlreadyExistsException("Captain with phone " + captainRegRequest.getPhone() + " is already registered.");
        }

        Vehicle vehicle = vehicleCreate(captainRegRequest);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        Captain captain = captainCreate(captainRegRequest);
        captain.setVehicle(savedVehicle);
        captain.setCreatedAt(LocalDateTime.now());

        return captainRepository.save(captain);
    }

    @Override
    public CaptainDTO getCaptainById(Long captainId) {
        return captainRepository.findById(captainId)
                .map(this::convertToDto)
                .orElseThrow(()-> new ResourceNotFoundException("Captain not exists."));
    }

    @Override
    public CaptainDTO getCaptainByEmail(String email) {
        return captainRepository.findByEmail(email)
                .map(this::convertToDto)
                .orElseThrow(()-> new ResourceNotFoundException("Captain not exists."));
    }

    @Override
    public CaptainDTO getCaptainByPhone(String phone) {
        return captainRepository.findByPhone(phone)
                .map(this::convertToDto)
                .orElseThrow(()-> new ResourceNotFoundException("Captain not exists."));
    }

    @Override
    public List<CaptainDTO> getAllCaptains() {
        List<Captain> captains = captainRepository.findAll();
        return captains.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public Captain updateCaptain(Long captainId, CaptainRegRequest captainRegRequest) {
        Captain existingCaptain = captainRepository.findById(captainId)
                .orElseThrow(() -> new ResourceNotFoundException("Captain with ID " + captainId + " not found."));

        if (!existingCaptain.getPhone().equals(captainRegRequest.getPhone()) &&
                captainRepository.existsByPhone(captainRegRequest.getPhone())) {
            throw new AlreadyExistsException("Phone number " + captainRegRequest.getPhone() + " is already registered.");
        }

        Vehicle existingVehicle = existingCaptain.getVehicle();
        existingVehicle.setModel(captainRegRequest.getModel());
        existingVehicle.setRegistrationNumber(captainRegRequest.getRegistrationNumber());
        existingVehicle.setRegistrationUrl(captainRegRequest.getRegistrationUrl());

        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);

        existingCaptain.setPhone(captainRegRequest.getPhone());
        existingCaptain.setEmail(captainRegRequest.getEmail());
        existingCaptain.setDrivingLicenseUrl(captainRegRequest.getDrivingLicenseUrl());
        existingCaptain.setVehicle(updatedVehicle);

        return captainRepository.save(existingCaptain);
    }

    @Override
    public void deleteCaptain(Long captainId) {
        captainRepository.findById(captainId).ifPresentOrElse(captainRepository::delete, () -> {
            throw new ResourceNotFoundException("Captain not found");
        });
    }

//    @Override
//    public void availableCaptain(Long captainId) {
//        Captain existingCaptain = captainRepository.findById(captainId)
//                .orElseThrow(() -> new ResourceNotFoundException("Captain with ID " + captainId + " not found."));
//        existingCaptain.setIsOnline(true);
//
//        captainRepository.save(existingCaptain);
//    }

    @Override
    public CaptainDTO convertToDto(Captain captain) {
        return modelMapper.map(captain, CaptainDTO.class);
    }

    @Override
    public void createCaptainQueue(Long captainId) {
        Queue queue = new Queue(rideOfferQueuePrefix+captainId);
        amqpAdmin.declareQueue(queue);
        Binding binding = BindingBuilder.bind(queue)
                .to(rabbitConfig.rideOffersExchange())
                .with(rideOfferRoutingKeyPrefix+captainId);
        amqpAdmin.declareBinding(binding);
    }

    @Override
    public void deleteCaptainQueue(Long captainId) {
        amqpAdmin.deleteQueue(rideOfferQueuePrefix+captainId);
    }

    @Override
    public APIResponse acceptRide(Long rideId, Long captainId) {
        // Call to ride-manager-service to update the ride status.
        return rideManagerClient.acceptedRideOffer(rideId, captainId);
    }

    private Vehicle vehicleCreate(CaptainRegRequest captainRegRequest) {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(captainRegRequest.getModel());
        vehicle.setRegistrationNumber(captainRegRequest.getRegistrationNumber());
        vehicle.setRegistrationUrl(captainRegRequest.getRegistrationUrl());
        return vehicle;
    }

    private Captain captainCreate(CaptainRegRequest captainRegRequest) {
        Captain captain = new Captain();
        captain.setEmail(captainRegRequest.getEmail());
        captain.setPhone(captainRegRequest.getPhone());
        captain.setPassword(captainRegRequest.getPassword());
        captain.setDrivingLicenseUrl(captainRegRequest.getDrivingLicenseUrl());
        return captain;
    }


}
