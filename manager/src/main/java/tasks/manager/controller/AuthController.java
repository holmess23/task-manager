package tasks.manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tasks.manager.dto.user.AuthResponseDTO;
import tasks.manager.dto.user.LoginDTO;
import tasks.manager.dto.user.RegisterDTO;
import tasks.manager.exception.ApiError;
import tasks.manager.service.user.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para registro y login de usuarios")
public class AuthController {
    private final AuthService authService;


    @Operation(
        summary = "Registro de usuario",
        description = "Crea una cuenta nueva y devuelve el token JWT para autenticación."+
                        "\nRequiere un JSON con 'username' y 'password'."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Usuario registrado correctamente",
            content = @Content(
                schema = @Schema(implementation = AuthResponseDTO.class),
                examples = @ExampleObject(
                    value = """
                        {
                          "token": "eyJhbGciOiJIUzI1NiJ9...",
                          "name": "Ana García",
                          "email": "ana@email.com",
                          "role": "USER"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o email ya registrado",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        )
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }

    @Operation(
        summary = "Login de usuario",
        description = "Autentica al usuario y devuelve el token JWT"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Login correcto, token en la respuesta",
            content = @Content(schema = @Schema(implementation = AuthResponseDTO.class),
            examples = @ExampleObject(
                        value = """
                            {
                            "token": "eyJhbGciOiJIUzI1NiJ9...",
                            "name": "Ana García",
                            "email": "ana@email.com",
                            "role": "USER"
                            }
                            """
                    ))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Email o contraseña incorrectos",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
