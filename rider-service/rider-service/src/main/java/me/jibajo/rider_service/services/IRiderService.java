package me.jibajo.rider_service.services;

import me.jibajo.rider_service.entities.Rider;

import java.util.List;
import java.util.Optional;

public interface IRiderService {
    Rider createRider(Rider rider);
    Optional<Rider> getRiderById(Long riderId);
    Optional<Rider> getRiderByEmail(String email);
    Optional<Rider> getRiderByPhone(String phone);
     List<Rider> getAllRiders();
     Rider updateRider(Long riderId, Rider updatedRider);
     void deleteRider(Long riderId);
}
