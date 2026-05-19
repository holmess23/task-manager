package tasks.manager.service.user;

import tasks.manager.dto.user.AuthResponseDTO;
import tasks.manager.dto.user.LoginDTO;
import tasks.manager.dto.user.RegisterDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterDTO dto);
    AuthResponseDTO login(LoginDTO dto);
}