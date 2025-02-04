package me.jibajo.admin_service.repositories;

import me.jibajo.admin_service.entities.UserManagementLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManagementLogRepository extends JpaRepository<UserManagementLog, Long> {
}
