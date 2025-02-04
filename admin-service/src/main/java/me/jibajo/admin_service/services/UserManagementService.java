package me.jibajo.admin_service.services;


import me.jibajo.admin_service.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManagementService {

    @Autowired
    private AdminRepository adminRepository;

    public void disableUser(Long userId) {
        // Logic to disable user
    }
}
