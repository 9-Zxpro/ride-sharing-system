package me.jibajo.captain_service.services;

import me.jibajo.captain_service.entities.Captain;
import me.jibajo.captain_service.repositories.CaptainRepository;

import java.util.List;
import java.util.Optional;

public class CaptainServiceImp implements ICaptainService {
    private final CaptainRepository captainRepository;

    public CaptainServiceImp(CaptainRepository captainRepository) {
        this.captainRepository = captainRepository;
    }

    public Captain createCaptain(Captain captain) {
        return captainRepository.save(captain);
    }

    public Optional<Captain> getCaptainById(Long captainId) {
        return captainRepository.findById(captainId);
    }

    public Optional<Captain> getCaptainByEmail(String email) {
        return captainRepository.findByEmail(email);
    }

    public Optional<Captain> getCaptainByPhone(String phone) {
        return captainRepository.findByPhone(phone);
    }

    public List<Captain> getAllCaptains() {
        return captainRepository.findAll();
    }

    public Captain updateCaptain(Long captainId, Captain updatedCaptain) {
        return captainRepository.findById(captainId).map(captain -> {
            captain.setEmail(updatedCaptain.getEmail());
            captain.setPhone(updatedCaptain.getPhone());
//            captain.setPassword(updatedCaptain.getPassword());
            captain.setVehicle(updatedCaptain.getVehicle());
            captain.setDrivingLicenseUrl(updatedCaptain.getDrivingLicenseUrl());
            captain.setIsAvailable(updatedCaptain.getIsAvailable());
            captain.setIsActive(updatedCaptain.getIsActive());
            return captainRepository.save(captain);
        }).orElseThrow(() -> new RuntimeException("Captain not found"));
    }

    public void deleteCaptain(Long captainId) {
        captainRepository.deleteById(captainId);
    }
}
