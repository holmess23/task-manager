package tasks.manager.config.docs;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(apiServers())
                .addSecurityItem(securityRequirement())
                .components(securityComponents());
    }

    private Components securityComponents() {
        SecurityScheme jwtScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Bearer Authentication")
                .description("Introduce el token JWT obtenido en /api/auth/login | register");
        return new Components().addSecuritySchemes("Bearer Authentication", jwtScheme);
    }

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList("Bearer Authentication");
    }

    private List<Server> apiServers() {
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Servidor local para desarrollo");
        return List.of(localServer);
    }

    private Info apiInfo() {
        return new Info()
                .title("Task Manager API")
                .description("API REST para gestión de tareas con autenticación JWT.\n" + //
                            "\n" + //
                            "## Autenticación\n" + //
                            "1. Registra un usuario en `/api/auth/register`\n" + //
                            "2. Haz login en `/api/auth/login` para obtener el token\n" + //
                            "3. Pulsa el botón **Authorize** e introduce: `Bearer <tu_token>`\n" + //
                            "4. Todos los endpoints protegidos usarán ese token automáticamente\n" + //
                            "\n" + //
                            "## Roles\n" + //
                            "- **USER**: puede gestionar sus propias tareas y categorías\n" + //
                            "- **ADMIN**: además puede eliminar cualquier categoría")
                .version("1.0.0");
    }
}
