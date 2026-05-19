package tasks.manager.dto.task;

import java.time.LocalDate;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(
    name = "UpdateTaskDTO",
    description = "Representación de una tarea para actualizar en el sistema." + 
                    "\nSe envían todos los campos, aunque no sean modificados."
)
public class UpdateTaskDTO {
    @Schema(
        description = "Identificador único de la tarea.",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;
    @NotBlank(message = "El título no puede estar vacío")
    @Schema(
        description = "Título de la tarea.",
        example = "Comprar leche",
        maxLength = 50
    )
    private String title;
    @Schema(
        description = "Descripción opcional de la tarea.",
        example = "Comprar leche en el supermercado",
        maxLength = 200
    )
    private String description;
    @NotNull(message = "La fecha no puede ser nula")
    @Schema(
        description = "Fecha límite para realizar la tarea." + 
                        "\nFormato ISO (YYYY-MM-DD)",
        example = "2026-12-01"
    )
    private LocalDate date;
    @NotBlank(message = "La prioridad es obligatoria.")
    @Schema(
        description = "Nivel de prioridad de la tarea.",
        example = "ALTA",
        allowableValues = {"BAJA", "MEDIA", "ALTA"}
    )
    private String priority;
    @Schema(
        description = "IDs de las categorías asociadas a la tarea.",
        example = "[1, 2, 3]"
    )
    private Set<Long> categories;
    @Schema(
        description = "Indica si la tarea está completada o no.",
        example = "false"
    )
    private boolean completed;
}
