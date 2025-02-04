package me.jibajo.admin_service.controller;

import lombok.RequiredArgsConstructor;
import me.jibajo.admin_service.services.UserManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserManagementController {

    private UserManagementService userManagementService;

    @PostMapping("/disable/{id}")
    public ResponseEntity<?> disableUser(@PathVariable Long id) {
        userManagementService.disableUser(id);
        return ResponseEntity.ok("User disabled successfully");
    }
}