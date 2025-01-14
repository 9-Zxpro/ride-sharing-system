package me.jibajo.rider_service.services;

import me.jibajo.rider_service.entities.Rider;
import me.jibajo.rider_service.repositories.RiderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RiderService implements IRiderService{

    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    public Rider createRider(Rider rider) {
        return riderRepository.save(rider);
    }

    public Optional<Rider> getRiderById(Long riderId) {
        return riderRepository.findById(riderId);
    }

    public Optional<Rider> getRiderByEmail(String email) {
        return riderRepository.findByEmail(email);
    }

    public Optional<Rider> getRiderByPhone(String phone) {
        return riderRepository.findByPhone(phone);
    }

    public List<Rider> getAllRiders() {
        return riderRepository.findAll();
    }

    public Rider updateRider(Long riderId, Rider updatedRider) {
        return riderRepository.findById(riderId).map(rider -> {
            rider.setEmail(updatedRider.getEmail());
            rider.setPhone(updatedRider.getPhone());
            return riderRepository.save(rider);
        }).orElseThrow(() -> new RuntimeException("Rider not found"));
    }

    public void deleteRider(Long riderId) {
        riderRepository.deleteById(riderId);
    }
}