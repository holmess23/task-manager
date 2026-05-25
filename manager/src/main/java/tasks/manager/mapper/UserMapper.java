package tasks.manager.mapper;

import tasks.manager.dto.user.AdminUserDTO;
import tasks.manager.model.user.User;

public class UserMapper {

    public static AdminUserDTO toDTO(User user, long taskCount) {
        return AdminUserDTO.builder()
                .id(user.getId())
                .name(user.getUsername())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .role(user.getRole().name())
                .taskCount(taskCount)
                .createdAt(user.getCreatedAt())
                .build();
    }

}