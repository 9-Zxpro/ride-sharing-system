package me.jibajo.captain_service.repositories;

import me.jibajo.captain_service.entities.Captain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaptainRepository extends JpaRepository<Captain, Long> {
    Optional<Captain> findByEmail(String email);
    Optional<Captain> findByPhone(String phone);

    boolean existsByPhone(String phone);
}
