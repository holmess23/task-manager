package tasks.manager.service.admin;

import java.util.List;

import tasks.manager.dto.user.AdminUserDTO;

public interface AdminService {
    List<AdminUserDTO> getAllUsers();
    AdminUserDTO getUserById(Long id);
    AdminUserDTO toggleUserEnabled(Long id);
    AdminUserDTO promoteToAdmin(Long id);
    AdminUserDTO demoteToUser(Long id);
    
}
