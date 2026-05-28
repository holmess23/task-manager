package tasks.manager.service.tasks;

import org.springframework.stereotype.Service;

import tasks.manager.dto.PageResponseDTO;
import tasks.manager.dto.TaskSearchDTO;
import tasks.manager.dto.task.CreateTaskDTO;
import tasks.manager.dto.task.TaskDTO;
import tasks.manager.dto.task.UpdateTaskDTO;


@Service
public interface TaskService {
    
    PageResponseDTO<TaskDTO> searchTasks(TaskSearchDTO criteria);
    TaskDTO getTaskById(Long id);
    TaskDTO createTask(CreateTaskDTO taskDTO);
    TaskDTO updateTask(Long id, UpdateTaskDTO taskDTO);
    void deleteTask(Long id);
}
