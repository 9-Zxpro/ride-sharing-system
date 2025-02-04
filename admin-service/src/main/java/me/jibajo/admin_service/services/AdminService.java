package me.jibajo.admin_service.services;

import lombok.RequiredArgsConstructor;
import me.jibajo.admin_service.dto.AdminDto;
import me.jibajo.admin_service.entities.Admin;
import me.jibajo.admin_service.repositories.AdminRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private AdminRepository adminRepository;

    public AdminDto createAdmin(AdminDto adminDto) {
        Admin admin = new Admin();
        admin.setName(adminDto.getName());
        admin.setPassword(adminDto.getPassword());
        admin.setEmail(adminDto.getEmail());
        admin.setRole(adminDto.getRole());
        adminRepository.save(admin);
        return adminDto;
    }
}
