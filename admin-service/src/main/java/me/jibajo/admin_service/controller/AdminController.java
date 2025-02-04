package me.jibajo.admin_service.controller;

import lombok.RequiredArgsConstructor;
import me.jibajo.admin_service.dto.AdminDto;
import me.jibajo.admin_service.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private AdminService adminService;

    @PostMapping("/create")
    public ResponseEntity<AdminDto> createAdmin(@RequestBody AdminDto adminDto) {
        return ResponseEntity.ok(adminService.createAdmin(adminDto));
    }
}