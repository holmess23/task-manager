package tasks.manager.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tasks.manager.model.task.Priority;

@Data
@Schema(description = "Criterios de búsqueda y filtrado de tareas")
public class SearchCriteriaDTO {
    
    @Schema(description = "Filtrar por estado", example = "true")
    private Boolean completed;

    @Schema(description = "Buscar por texto en el título", example = "comprar leche")
    private String search;

    @Schema(description = "Filtrar por prioridad", example = "ALTA")
    private Priority priority;

    @Schema(description = "Filtrar por categoría", example = "1")
    private Long categoryId;

    @Schema(description = "Filtrar tareas con fecha de vencimiento anterior o igual a esta fecha", example = "2024-12-31")
    private LocalDate dueBefore;

    @Schema(description = "Filtrar tareas con fecha de vencimiento posterior o igual a esta fecha", example = "2024-01-01")
    private LocalDate dueAfter;

    @Schema(description = "Filtrar tareas atrasadas (fecha de vencimiento pasada y no completada)", example = "true")
    private Boolean overdue;
}
