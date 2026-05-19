package tasks.manager.dto.task;

import java.time.LocalDate;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
    name = "CreateTaskDTO", 
    description = "Datos necesarios para crear una nueva tarea"
)
public class CreateTaskDTO {
     private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 100, message = "El título no puede tener más de 100 caracteres")
    @Schema(
        description = "Título de la tarea", 
        example = "Comprar leche",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String title;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    @Schema(
        description = "Descripción opcional de la tarea",
        example = "Comprar leche en el supermercado"
    )
    private String description;

    @NotNull(message = "La fecha no puede ser nula")
    @FutureOrPresent(message = "La fecha no puede ser anterior a hoy")
    @Schema(
        description = "Fecha límite para realizar la tarea." + 
                        "\nFormato ISO (YYYY-MM-DD)",
        example = "2026-12-01",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate date;

    @NotBlank(message =  "La prioridad es obligatoria.")
    @Schema(
        description = "Nivel de prioridad de la tarea.",
        example = "ALTA",
        requiredMode = Schema.RequiredMode.REQUIRED,
        allowableValues = {"BAJA", "MEDIA", "ALTA"}
    )
    private String priority;
    @Schema(
        description = "IDs de las categorías asociadas a la tarea",
        example = "[1, 2, 3]"
    )
    private Set<Long> categories;
    
}
