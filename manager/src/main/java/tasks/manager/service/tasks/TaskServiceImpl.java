package tasks.manager.service.tasks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tasks.manager.dto.SearchCriteriaDTO;
import tasks.manager.dto.category.CategoryDTO;
import tasks.manager.dto.task.CreateTaskDTO;
import tasks.manager.dto.task.TaskDTO;
import tasks.manager.dto.task.UpdateTaskDTO;
import tasks.manager.event.TaskCompletedEvent;
import tasks.manager.event.TaskCreatedEvent;
import tasks.manager.exception.TaskNotFoundException;
import tasks.manager.model.category.Category;
import tasks.manager.model.task.Priority;
import tasks.manager.model.task.Task;
import tasks.manager.model.user.User;
import tasks.manager.repository.CategoryRepository;
import tasks.manager.repository.tasks.TaskRepository;
import tasks.manager.repository.tasks.TaskSpecifications;
import tasks.manager.util.AuthUtil;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final AuthUtil authUtil;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public List<TaskDTO> searchTasks(SearchCriteriaDTO criteria) {
        User user = authUtil.getCurrentUser();
        
        Specification<Task> spec = TaskSpecifications.belongsToUser(user);

        if(criteria.getCompleted() != null) {
            spec = spec.and(TaskSpecifications.hasCompleted(criteria.getCompleted()));
        }

        if(criteria.getSearch() != null && !criteria.getSearch().isBlank()) {
            spec = spec.and(TaskSpecifications.titleContains(criteria.getSearch()));
        }

        if(criteria.getPriority() != null) {
            spec = spec.and(TaskSpecifications.hasPriority(criteria.getPriority()));
        }

        if(criteria.getDueBefore() != null) {
            spec = spec.and(TaskSpecifications.dueBefore(criteria.getDueBefore()));
        }

        if(criteria.getDueAfter() != null) {
            spec = spec.and(TaskSpecifications.dueAfter(criteria.getDueAfter()));
        }

        if(Boolean.TRUE.equals(criteria.getOverdue())) {
            spec = spec.and(TaskSpecifications.isOverdue());
        }
        return taskRepository.findAll(spec).stream().map(this::toDTO).toList();
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
        .orElseThrow(() ->
            new TaskNotFoundException(id)
        );
        return toDTO(task);
                                
    }

    @Override
    public TaskDTO createTask(CreateTaskDTO taskDTO) {
        User user = authUtil.getCurrentUser();
        Set<Category> categories = resolveCategoryIds(taskDTO.getCategories());

        Task task = toEntity(taskDTO);
        task.setUser(user);
        task.setCategories(categories);

        Task savedTask = taskRepository.save(task);
        eventPublisher.publishEvent(new TaskCreatedEvent(this, savedTask, user));
        return toDTO(savedTask);
    }

    @Override
    public TaskDTO updateTask(Long id, UpdateTaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException(id));

        boolean wasCompleted = existingTask.isCompleted();
        
        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setDate(taskDTO.getDate());
        existingTask.setPriority(Priority.valueOf(taskDTO.getPriority()));
        existingTask.setCategories(resolveCategoryIds(taskDTO.getCategories()));
        existingTask.setCompleted(taskDTO.isCompleted());
        Task updatedTask = taskRepository.save(existingTask);

        if(!wasCompleted && updatedTask.isCompleted()) {
            User user = authUtil.getCurrentUser();
            eventPublisher.publishEvent(new TaskCompletedEvent(this, updatedTask, user));
        }
        return toDTO(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        User user = authUtil.getCurrentUser();
        if(!taskRepository.existsByIdAndUser(id, user)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    private TaskDTO toDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setDate(task.getDate());
        dto.setPriority(task.getPriority().toString());
        dto.setCompleted(task.isCompleted());

        Set<CategoryDTO> categoryDTOs = task.getCategories()
                .stream()
                .map(c -> {
                    CategoryDTO cDTO = new CategoryDTO();
                    cDTO.setId(c.getId());
                    cDTO.setColor(c.getColor());
                    cDTO.setName(c.getName());
                    return cDTO;
                })
                .collect(Collectors.toSet());
        
        dto.setCategories(categoryDTOs);

        return dto;
    }

    private Task toEntity(CreateTaskDTO dto) {
        Set<Category> categories = resolveCategoryIds(dto.getCategories());
        return new Task(
            dto.getTitle(),
            dto.getDescription(),
            dto.getDate(),
            Priority.valueOf(dto.getPriority()),
            categories
        );
    }

    private Set<Category> resolveCategoryIds(Set<Long> ids){
        if(ids == null || ids.isEmpty()) return new HashSet<>();
        return categoryRepository.findByIdIn(ids);
    }

    
}
