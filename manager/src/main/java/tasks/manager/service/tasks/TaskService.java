package tasks.manager.service.tasks;

import java.util.List;

import org.springframework.stereotype.Service;

import tasks.manager.dto.SearchCriteriaDTO;
import tasks.manager.dto.task.CreateTaskDTO;
import tasks.manager.dto.task.TaskDTO;
import tasks.manager.dto.task.UpdateTaskDTO;


@Service
public interface TaskService {
    
    List<TaskDTO> searchTasks(SearchCriteriaDTO criteria);
    TaskDTO getTaskById(Long id);
    TaskDTO createTask(CreateTaskDTO taskDTO);
    TaskDTO updateTask(Long id, UpdateTaskDTO taskDTO);
    void deleteTask(Long id);
}
