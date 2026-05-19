// /***package tasks.manager.controller;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
// import org.springframework.context.annotation.Import;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;

// import java.time.LocalDate;
// import java.util.List;

// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import tasks.manager.dto.task.CreateTaskDTO;
// import tasks.manager.dto.task.TaskDTO;
// import tasks.manager.exception.GlobalExceptionHandler;
// import tasks.manager.service.tasks.TaskService;

// @WebMvcTest(
//     controllers = TaskController.class
// )
// @Import(GlobalExceptionHandler.class)
// @DisplayName("Test del controlador de tareas.")
// public class TaskControllerTest {
//     @Autowired
//     private MockMvc mockMvc;

//     @MockitoBean
//     private TaskService service;

//     private TaskDTO sampleTaskDTO;
//     private CreateTaskDTO sampleCreateDTO;

//     @BeforeEach
//     void setUp() {

//         sampleTaskDTO = new TaskDTO();
//         sampleTaskDTO.setId(1L);
//         sampleTaskDTO.setTitle("Estudiar Spring Boot");
//         sampleTaskDTO.setDescription("Tests de integración");
//         sampleTaskDTO.setDate(LocalDate.of(2026, 12, 31));
//         sampleTaskDTO.setPriority("ALTA");
//         //sampleTaskDTO.setCategories(List.of("estudio"));
//         sampleTaskDTO.setCompleted(false);

//         sampleCreateDTO = new CreateTaskDTO();
//         sampleCreateDTO.setTitle("Estudiar Spring Boot");
//         sampleCreateDTO.setDescription("Tests de integración");
//         sampleCreateDTO.setDate(LocalDate.of(2026, 12, 31));
//         sampleCreateDTO.setPriority("ALTA");
//         //sampleCreateDTO.setCategories(List.of("estudio"));
//     }

//     /**
//      * TEST DE MÉTODO GET
//      */

//     @Test
//     @DisplayName("GET /api/tasks devuelve 200 con lista de tareas.")
//     void getTasks200DTOList() throws Exception{
//         when(service.getAllTasks()).thenReturn(List.of(sampleTaskDTO));
        
//         mockMvc.perform(get("/api/tasks"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$").isArray())
//                 .andExpect(jsonPath("$.length()").value(1))
//                 .andExpect(jsonPath("$[0].title").value("Estudiar Spring Boot"))
//                 .andExpect(jsonPath("$[0].priority").value("ALTA"));

//         verify(service, times(1)).getAllTasks();
//     }

//     @Test
//     @DisplayName("GET /api/tasks?completed=true filtra tareas completadas")
//     void getTasksCompleted200FilteredList() throws Exception{
//         when(service.getTasksByStatus(true)).thenReturn(List.of());

//         mockMvc.perform(get("/api/tasks").param("completed", "true"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$").isArray())
//                 .andExpect(jsonPath("$.length()").value(0));

//         verify(service, times(1)).getTasksByStatus(true);
//         verify(service, never()).getAllTasks();
//     }

//     @Test
//     @DisplayName("GET /api/tasks?completed=false filtra tareas no completadas")
//     void getTasksPending200FilteredList() throws Exception{
//         when(service.getTasksByStatus(false)).thenReturn(List.of(sampleTaskDTO));

//         mockMvc.perform(get("/api/tasks").param("completed", "false"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(jsonPath("$").isArray())
//                 .andExpect(jsonPath("$.length()").value(1))
//                 .andExpect(jsonPath("$[0].completed").value("false"));

//         verify(service, times(1)).getTasksByStatus(false);
//         verify(service, never()).getAllTasks();
//     }


// }
// ***/