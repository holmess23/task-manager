package tasks.manager.service.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tasks.manager.dto.user.AuthResponseDTO;
import tasks.manager.dto.user.LoginDTO;
import tasks.manager.dto.user.RegisterDTO;
import tasks.manager.model.user.User;
import tasks.manager.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    
    @Override
    public AuthResponseDTO register(RegisterDTO dto) {
        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
        userRepository.save(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("role", user.getRole().name());
        return new AuthResponseDTO(jwtService.generateToken(claims, user), user.getName(), user.getEmail(), user.getRole().name());
    }

    @Override
    public AuthResponseDTO login(LoginDTO dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("role", user.getRole().name());
        return new AuthResponseDTO(jwtService.generateToken(claims, user), user.getName(), user.getEmail(), user.getRole().name());
    }
    
}
