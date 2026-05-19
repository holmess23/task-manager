package tasks.manager.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tasks.manager.dto.SearchCriteriaDTO;
import tasks.manager.dto.task.CreateTaskDTO;
import tasks.manager.dto.task.TaskDTO;
import tasks.manager.dto.task.UpdateTaskDTO;
import tasks.manager.exception.ApiError;
import tasks.manager.model.task.Priority;
import tasks.manager.service.tasks.TaskService;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tareas", description = "Endpoints para gestión de tareas")
public class TaskController {

    private final TaskService service; 

    @Operation(
        summary = "Obtener tareas",
        description = "Obtiene todas las tareas de la base de datos del usuario autenticado"+
        "\nSin parámetros devuelve todas las tareas."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Se han obtenido las tareas",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TaskDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT ausente o inválido",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks(
        @Parameter(description = "Filtrar por estado de completado", example = "true")
        @RequestParam(required = false) Boolean completed,

        @Parameter(description = "Buscar por texto en título", example = "spring")
        @RequestParam(required = false) String search,

        @Parameter(description = "Filtrar por prioridad", example = "ALTA")
        @RequestParam(required = false) Priority priority,
    
        @Parameter(description = "Filtrar por categoría (ID)", example = "1")
        @RequestParam(required = false) Long categoryId,

        @Parameter(description = "Filtrar tareas con fecha de vencimiento anterior o igual a esta fecha", example = "2024-12-31")
        @RequestParam(required = false) LocalDate dueBefore,

        @Parameter(description = "Filtrar tareas con fecha de vencimiento posterior o igual a esta fecha", example = "2024-01-01")
        @RequestParam(required = false) LocalDate dueAfter,

        @Parameter(description = "Filtrar tareas atrasadas (fecha de vencimiento pasada y no completada)", example = "true")
        @RequestParam(required = false) Boolean overdue
    ) {
        SearchCriteriaDTO criteria = new SearchCriteriaDTO();
        criteria.setCompleted(completed);
        criteria.setSearch(search);
        criteria.setPriority(priority);
        criteria.setCategoryId(categoryId);
        criteria.setDueBefore(dueBefore);
        criteria.setDueAfter(dueAfter);
        criteria.setOverdue(overdue);

        
        return ResponseEntity.ok(service.searchTasks(criteria));
    }

    @Operation(
        summary = "Obtener tarea por ID",
        description = "Obtiene una tarea específica por su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Se ha obtenido la tarea",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TaskDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT ausente o inválido",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Tarea no encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(
        @Parameter(description = "ID de la tarea a obtener", example = "1")
        @PathVariable Long id){
        return ResponseEntity.ok(service.getTaskById(id));
    }

    @Operation(
        summary = "Crear nueva tarea",
        description = "Crea una nueva tarea con los datos proporcionados"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Tarea creada correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TaskDTO.class),
                examples = @ExampleObject(
                    name = "Ejemplo de respuesta",
                    value = """
                        {
                          "id": 1,
                          "title": "Estudiar Spring Boot",
                          "description": "Ver los tests de integración",
                          "date": "2026-12-01",
                          "priority": "ALTA",
                          "categories": [],
                          "completed": false
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT ausente o inválido",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos para crear una nueva tarea",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CreateTaskDTO.class),
                examples = @ExampleObject(
                    name = "Ejemplo de solicitud",
                    value = """
                        {
                          "title": "Estudiar Spring Boot",
                          "description": "Ver los tests de integración",
                          "date": "2026-12-01",
                          "priority": "ALTA",
                          "categories": []
                        }
                        """
                )
            )
        )
        @Valid @RequestBody CreateTaskDTO taskDTO) {
        TaskDTO createdTask = service.createTask(taskDTO);
        return ResponseEntity.status(201).body(createdTask);
    }

    @Operation(
        summary = "Actualizar tarea existente",
        description = "Actualiza una tarea existente con los datos proporcionados"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Tarea actualizada correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TaskDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT ausente o inválido",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Tarea no encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
        @Parameter(description = "ID de la tarea a actualizar", example = "1")
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos para actualizar la tarea",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UpdateTaskDTO.class),
                examples = @ExampleObject(
                    name = "Ejemplo de solicitud",
                    value = """
                        {
                          "title": "Estudiar Spring Boot - Actualizado",
                          "description": "Ver los tests de integración y agregar más ejemplos",
                          "date": "2026-12-02",
                          "priority": "MEDIA",
                          "categories": [],
                          "completed": true
                        }
                        """
                )
            )
        )
        @Valid @RequestBody UpdateTaskDTO taskDTO) {
        TaskDTO updatedTask = service.updateTask(id, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(
        summary = "Eliminar tarea",
        description = "Elimina una tarea existente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Tarea eliminada correctamente"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token JWT ausente o inválido",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Tarea no encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiError.class)
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
        @Parameter(description = "ID de la tarea a eliminar", example = "1")
        @PathVariable Long id
    ){
        service.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    
}
