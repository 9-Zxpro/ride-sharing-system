package me.jibajo.captain_service.services.captain;

import me.jibajo.captain_service.dto.APIResponse;
import me.jibajo.captain_service.dto.CaptainDTO;
import me.jibajo.captain_service.entities.Captain;
import me.jibajo.captain_service.dto.CaptainRegRequest;

import java.util.List;

public interface ICaptainService {

     Captain createCaptain(CaptainRegRequest captainRegRequest);

     CaptainDTO getCaptainById(Long captainId);

     CaptainDTO getCaptainByEmail(String email);

     CaptainDTO getCaptainByPhone(String phone);

     List<CaptainDTO> getAllCaptains();

     Captain updateCaptain(Long captainId, CaptainRegRequest captainRegRequest);

     void deleteCaptain(Long captainId);

    CaptainDTO convertToDto(Captain captain);

    void createCaptainQueue(Long captainId);

    void deleteCaptainQueue(Long captainId);

    APIResponse acceptRide(Long rideId, Long captainId);
}
