package me.jibajo.captain_service.services.captain;

import me.jibajo.captain_service.dto.CaptainDTO;
import me.jibajo.captain_service.entities.Captain;
import me.jibajo.captain_service.entities.Vehicle;
import me.jibajo.captain_service.requests.CaptainRegRequest;

import java.util.List;
import java.util.Optional;

public interface ICaptainService {

     Captain createCaptain(CaptainRegRequest captainRegRequest);

     CaptainDTO getCaptainById(Long captainId);

     CaptainDTO getCaptainByEmail(String email);

     CaptainDTO getCaptainByPhone(String phone);

     List<CaptainDTO> getAllCaptains();

     Captain updateCaptain(Long captainId, CaptainRegRequest captainRegRequest);

     void deleteCaptain(Long captainId);

     CaptainDTO convertToDto(Captain captain);
}
