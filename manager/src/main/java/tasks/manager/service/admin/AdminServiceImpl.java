package tasks.manager.service.admin;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tasks.manager.dto.user.AdminUserDTO;
import tasks.manager.mapper.UserMapper;
import tasks.manager.model.user.Role;
import tasks.manager.model.user.User;
import tasks.manager.repository.UserRepository;
import tasks.manager.repository.tasks.TaskRepository;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository; 
    private final TaskRepository taskRepository;   

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminUserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> 
            UserMapper.toDTO(user, taskRepository.countByUserId(user.getId()))
        ).toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public AdminUserDTO getUserById( Long id) {
        return userRepository.findById(id)
                .map(user -> UserMapper.toDTO(user, taskRepository.countByUserId(user.getId())))
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public AdminUserDTO toggleUserEnabled( Long id) {
        User userToToggle = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        userToToggle.setEnabled(!userToToggle.isEnabled());
        userToToggle = userRepository.save(userToToggle);
        return UserMapper.toDTO(userToToggle, taskRepository.countByUserId(userToToggle.getId()));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public AdminUserDTO promoteToAdmin(Long id) {
        User userToPromote = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        userToPromote.setRole(Role.ADMIN);
        userToPromote = userRepository.save(userToPromote);
        return UserMapper.toDTO(userToPromote, taskRepository.countByUserId(userToPromote.getId()));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public AdminUserDTO demoteToUser(Long id) {
        User userToDemote = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        userToDemote.setRole(Role.USER);
        userToDemote = userRepository.save(userToDemote);
        return UserMapper.toDTO(userToDemote, taskRepository.countByUserId(userToDemote.getId()));
    }
    
}
