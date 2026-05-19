package tasks.manager.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(
    name = "ApiError",
    description = "Estructura de respuesta para errores en la API."
)
public class ApiError {
    @Schema(
        description = "Código de estado HTTP del error.",
        example = "400"
    )
    private int status;
    @Schema(
        description = "Mensaje descriptivo del error.",
        example = "Error de validación"
    )
    private String message;
    @Schema(
        description = "Marca de tiempo indicando cuándo ocurrió el error.",
        example = "2024-06-01T12:34:56"
    )
    private LocalDateTime timestamp;

    @Schema(
        description = "Mapa de errores de validación, donde la clave es el nombre del campo y el valor es una lista de mensajes de error asociados a ese campo.",
        example = "{\"title\": [\"El título no puede estar vacío\"], \"date\": [\"La fecha no puede ser anterior a hoy\"]}"
    )
    private Map<String, List<String>> fieldErrors;

    public static ApiError of(int status, String message){
        return ApiError.builder()
        .status(status)
        .message(message)
        .timestamp(LocalDateTime.now())
        .build();
    }

    public static ApiError ofValidation(Map<String, List<String>> fieldErrors){
        return ApiError.builder()
                .status(400)
                .message("Error de validación")
                .timestamp(LocalDateTime.now())
                .fieldErrors(fieldErrors)
                .build();
    }
}
