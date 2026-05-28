package tasks.manager.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tasks.manager.model.task.Priority;
import tasks.manager.model.user.User;

@Data
@Schema(description = "Criterios de búsqueda y filtrado de tareas")
public class TaskSearchDTO {

    @Schema(description = "Filtrar por usuario")
    private User user;
    
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

    @Schema(description = "Número de página (0-indexed)", example = "0")
    private int page = 0;

    @Schema(description = "Tamaño de página", example = "20")
    private int size = 20;

    @Schema(description = "Campo para ordenar", example = "dueDate")
    private String sortBy = "date";

    @Schema(description = "Dirección de ordenamiento", example = "asc")
    private String sortDir = "asc";

    public Pageable toPageable() {
        List<String> validFields = List.of("date", "priority", "title", "completed");

        String field = (sortBy != null && validFields.contains(sortBy)) ? sortBy : "date";
        Sort sort = (sortDir != null && sortDir.equalsIgnoreCase("asc")) 
        ? Sort.by(field).ascending() : Sort.by(field).descending();

        int safePage = Math.max(0, page);
    int safeSize = Math.min(Math.max(1, size), 50);

        return PageRequest.of(safePage, safeSize, sort);
    }
}
