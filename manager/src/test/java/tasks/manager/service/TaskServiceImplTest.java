package tasks.manager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tasks.manager.dto.task.CreateTaskDTO;
import tasks.manager.dto.task.TaskDTO;
import tasks.manager.model.category.Category;
import tasks.manager.model.task.Priority;
import tasks.manager.model.task.Task;
import tasks.manager.repository.tasks.TaskRepository;
import tasks.manager.service.tasks.TaskServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests del servicio de tareas")
public class TaskServiceImplTest {
    
    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskServiceImpl service;

    private Task task;
    private CreateTaskDTO dto;

    @BeforeEach
    void setUp(){

        task = Task.builder()
        .title("Estudiar Spring Boot")
        .description("Ver los tests")
        .date(LocalDate.of(2026, 12, 31))
        .priority(Priority.ALTA)
        .categories(Set.of(new Category("estudio", "#fff")))
        .build();

        task.forceId(1L);

        dto = new CreateTaskDTO();
        dto.setTitle("Estudiar Spring Boot");
        dto.setDescription("Ver los tests");
        dto.setDate(LocalDate.of(2026, 12, 31));
        dto.setPriority("ALTA");
        //dto.setCategories(List.of("estudio"));
    }


    /**
     * TESTS DE getAllTasks()
     */

    @Test
    @DisplayName("getAllTasks devuelve lista de DTOs cuando hay tareas")
    void getAllTasksWhenExist(){
        when(repository.findAll()).thenReturn(List.of(task));
        List<TaskDTO> result = List.of();
        //List<TaskDTO> result = service.getAllTasks();

        assertTrue(result.size() == 1);
        assertTrue(result.get(0).getTitle().equals("Estudiar Spring Boot"));
        assertTrue(result.get(0).getPriority().equals("ALTA"));

        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllTasks devuelve lista vacía cuando no hay tareas")
    void getAllTasksWhenNotExist(){
        when(repository.findAll()).thenReturn(List.of());
        
        List<TaskDTO> result = List.of();
        //List<TaskDTO> result = service.getAllTasks();

        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    /**
     * TESTS DE getTaskById()
     */

    @Test
    @DisplayName("getTaskById devuelve la tarea cuando existe")
    void getTaskByIDWhenExist(){
        when(repository.findById(1L)).thenReturn(Optional.of(task));

        TaskDTO result = service.getTaskById(1L);

        assertNotNull(result);
        assertEquals(result.getId(), 1L);
        assertEquals(result.getTitle(), "Estudiar Spring Boot");

        verify(repository, times(1)).findById(1L);
    }

    // TODO: getTaskById lanza TaskNotFoundException cuando no existe


    /**
     * TESTS DE createTask()
     */

    @Test
    @DisplayName("createTask guarda y devuelve la tarea creada")
    void createTasksSavesAndReturns(){
        when(repository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = service.createTask(dto);

        assertNotNull(result);
        assertEquals(result.getId(), 1L);
        assertEquals(result.getTitle(), "Estudiar Spring Boot");
        verify(repository, times(1)).save(any(Task.class));
    }

    // TODO: createTask lanza excepción si el título está vacío

    /**
     * TESTS DE deleteTask()
     */

    // TODO: deleteTask elimina la tarea cuando existe

    // TODO: deleteTask lanza excepción cuando la tarea no existe

    /**
     * TESTS DE completeTask()
     */

    // TODO: updateTask marca la tarea como completada correctamente

    /**
     * TESTS DE searchByTitle()
     */

    // TODO: searchByTitle devuelve tareas que contienen el texto

    // TODO: searchByTitle devuelve lista vacía cuando no hay coincidencias

}
