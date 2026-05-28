package tasks.manager.service.tasks;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tasks.manager.dto.PageResponseDTO;
import tasks.manager.dto.TaskSearchDTO;
import tasks.manager.dto.task.CreateTaskDTO;
import tasks.manager.dto.task.TaskDTO;
import tasks.manager.dto.task.UpdateTaskDTO;
import tasks.manager.event.TaskCompletedEvent;
import tasks.manager.event.TaskCreatedEvent;
import tasks.manager.exception.TaskNotFoundException;
import tasks.manager.mapper.TaskMapper;
import tasks.manager.model.task.Task;
import tasks.manager.model.user.User;
import tasks.manager.repository.tasks.SpecificationsBuilder;
import tasks.manager.repository.tasks.TaskRepository;
import tasks.manager.util.AuthUtil;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    
    private final TaskRepository taskRepository;
    private final AuthUtil authUtil;
    private final ApplicationEventPublisher eventPublisher;
    private final TaskMapper taskMapper;

    @Override
    public PageResponseDTO<TaskDTO> searchTasks(TaskSearchDTO criteria) {
        User user = authUtil.getCurrentUser();
        criteria.setUser(user);
        Specification<Task> spec = SpecificationsBuilder.build(criteria);

        Page<Task> taskPage = taskRepository.findAll(spec, criteria.toPageable());
        Page<TaskDTO> dtoPage = taskPage.map(taskMapper::toDTO);
        return PageResponseDTO.of(dtoPage);
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
        .orElseThrow(() ->
            new TaskNotFoundException(id)
        );
        return taskMapper.toDTO(task);
                                
    }

    @Override
    public TaskDTO createTask(CreateTaskDTO taskDTO) {
        User user = authUtil.getCurrentUser();

        Task task = taskMapper.toEntity(taskDTO);
        task.setUser(user);

        Task savedTask = taskRepository.save(task);
        eventPublisher.publishEvent(new TaskCreatedEvent(this, savedTask, user));
        return taskMapper.toDTO(savedTask);
    }

    @Override
    public TaskDTO updateTask(Long id, UpdateTaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException(id));

        boolean wasCompleted = existingTask.isCompleted();
        
        taskMapper.updateEntity(taskDTO, existingTask);

        Task updatedTask = taskRepository.save(existingTask);

        if(!wasCompleted && updatedTask.isCompleted()) {
            User user = authUtil.getCurrentUser();
            eventPublisher.publishEvent(new TaskCompletedEvent(this, updatedTask, user));
        }
        return taskMapper.toDTO(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        User user = authUtil.getCurrentUser();
        if(!taskRepository.existsByIdAndUser(id, user)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}
