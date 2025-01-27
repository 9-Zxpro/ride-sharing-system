package me.jibajo.rider_service.repositories;


import me.jibajo.rider_service.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {
    Optional<Rider> findByEmail(String email);
    Optional<Rider> findByPhone(String phone);
    Boolean existsByPhone(String phone);
}
