package me.jibajo.rider_service.services;

import me.jibajo.rider_service.dto.RiderDTO;
import me.jibajo.rider_service.dto.RiderPhoneDto;
import me.jibajo.rider_service.entities.Rider;

import java.util.List;
import java.util.Optional;

public interface IRiderService {
    Rider createRider(RiderPhoneDto riderPhoneDto);

    RiderDTO getRiderById(Long riderId);

    RiderDTO getRiderByEmail(String email);

    RiderDTO getRiderByPhone(String phone);

    List<RiderDTO> getAllRiders();

    Rider updateRider(Long riderId, Rider updatedRider);

    void deleteRider(Long riderId);

    RiderDTO converToDto(Rider rider);
}
