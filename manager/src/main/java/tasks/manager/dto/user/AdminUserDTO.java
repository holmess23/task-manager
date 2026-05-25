package tasks.manager.dto.user;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private boolean enabled;
    private long taskCount;
    private LocalDateTime createdAt;
}
