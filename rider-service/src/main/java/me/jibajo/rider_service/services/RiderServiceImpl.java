package me.jibajo.rider_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.rider_service.dto.RiderDTO;
import me.jibajo.rider_service.dto.RiderPhoneDto;
import me.jibajo.rider_service.entities.Rider;
import me.jibajo.rider_service.exceptions.AlreadyExistsException;
import me.jibajo.rider_service.exceptions.ResourceNotFoundException;
import me.jibajo.rider_service.repositories.RiderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements IRiderService{

    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;

    @Override
    public Rider createRider(RiderPhoneDto riderPhoneDto) {
        if(riderRepository.existsByPhone(riderPhoneDto.getPhone())){
            throw new AlreadyExistsException("Rider with phone " + riderPhoneDto.getPhone() + " is already registered.");
        }
        Rider rider = new Rider();
        rider.setPhone(riderPhoneDto.getPhone());
        rider.setPhoneVerified(true);
        rider.setCreatedAt(LocalDateTime.now());

        return riderRepository.save(rider);
    }

    @Override
    public RiderDTO getRiderById(Long riderId) {
        return riderRepository.findById(riderId)
                .map(this::converToDto)
                .orElseThrow(()-> new ResourceNotFoundException("Rider not found"));
    }

    @Override
    public RiderDTO getRiderByEmail(String email) {
        return riderRepository.findByEmail(email)
                .map(this::converToDto)
                .orElseThrow(()-> new ResourceNotFoundException("Rider not found"));
    }

    @Override
    public RiderDTO getRiderByPhone(String phone) {
        return riderRepository.findByPhone(phone)
                .map(this::converToDto)
                .orElseThrow(()-> new ResourceNotFoundException("Rider not found"));
    }

    @Override
    public List<RiderDTO> getAllRiders() {
        List<Rider> riders = riderRepository.findAll();
        return riders.stream()
                .map(this::converToDto)
                .toList();
    }

    @Override
    public Rider updateRider(Long riderId, Rider updatedRider) {
        return riderRepository.findById(riderId).map(rider -> {
            rider.setEmail(updatedRider.getEmail());
            rider.setPhone(updatedRider.getPhone());
            return riderRepository.save(rider);
        }).orElseThrow(() -> new RuntimeException("Rider not found"));
    }

    @Override
    public void deleteRider(Long riderId) {
        riderRepository.findById(riderId).ifPresentOrElse(riderRepository::delete, ()->{
            throw new ResourceNotFoundException("Rider not exists");
        });
    }

    @Override
    public RiderDTO converToDto(Rider rider) {
        return modelMapper.map(rider, RiderDTO.class);
    }
}