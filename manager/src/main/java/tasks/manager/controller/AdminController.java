package tasks.manager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import tasks.manager.dto.user.AdminUserDTO;
import tasks.manager.service.admin.AdminService;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Endpoints para administración de usuarios")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<AdminUserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @PutMapping("/users/{id}/toggle-enabled")
    public ResponseEntity<AdminUserDTO> toggleUserEnabled(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.toggleUserEnabled(id));
    }

    @PutMapping("/users/{id}/promote")
    public ResponseEntity<AdminUserDTO> promoteToAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.promoteToAdmin(id));
    }

    @PutMapping("/users/{id}/demote")
    public ResponseEntity<AdminUserDTO> demoteToUser(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.demoteToUser(id));
    }

}
