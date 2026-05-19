package tasks.manager.dto.task;

import java.time.LocalDate;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tasks.manager.dto.category.CategoryDTO;

@Data
@Schema(
    name = "TaskDTO",
    description = "Representación de una tarea del sistema."
)
public class TaskDTO {
    
    @Schema(
        description = "Identificador único de la tarea.",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 50, message = "El título no puede superar los 50 caracteres.")
    @Schema(
        description = "Título de la tarea.",
        example = "Comprar leche",
        maxLength = 50,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String title;

    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres.")
    @Schema(
        description = "Descripción opcional de la tarea.",
        example = "Comprar leche en el supermercado",
        maxLength = 200
    )
    private String description;

    @NotNull(message = "La fecha no puede ser nula")
    @FutureOrPresent(message = "La fecha no puede ser anterior a hoy")
    @Schema(
        description = "Fecha límite para realizar la tarea." + 
                        "\nFormato ISO (YYYY-MM-DD)",
        example = "2026-12-01"
    )
    private LocalDate date;

    @NotBlank(message =  "La prioridad es obligatoria.")
    @Schema(
        description = "Nivel de prioridad de la tarea.",
        example = "ALTA",
        allowableValues = {"BAJA", "MEDIA", "ALTA"}
    )
    private String priority;
    @Schema(
        description = "Categorías asociadas a la tarea",
        example = "[1, 2, 3]"
    )
    private Set<CategoryDTO> categories;
    @Schema(
        description = "Indica si la tarea ha sido completada.",
        example = "false",
        defaultValue = "false"
    )
    private boolean completed;
}
