package me.jibajo.captain_service.services;

import me.jibajo.captain_service.entities.Captain;

import java.util.List;
import java.util.Optional;

public interface ICaptainService {
     Captain createCaptain(Captain captain);

     Optional<Captain> getCaptainById(Long captainId);

     Optional<Captain> getCaptainByEmail(String email);

     Optional<Captain> getCaptainByPhone(String phone);

     List<Captain> getAllCaptains();

     Captain updateCaptain(Long captainId, Captain updatedCaptain);

     void deleteCaptain(Long captainId);
}
