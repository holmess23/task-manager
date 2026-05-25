package tasks.manager.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import tasks.manager.dto.category.CategoryDTO;
import tasks.manager.dto.task.CreateTaskDTO;
import tasks.manager.dto.task.TaskDTO;
import tasks.manager.dto.task.UpdateTaskDTO;
import tasks.manager.model.category.Category;
import tasks.manager.model.task.Priority;
import tasks.manager.model.task.Task;
import tasks.manager.repository.CategoryRepository;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final CategoryRepository categoryRepository;

    public TaskDTO toDTO(Task task) {
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

    public Task toEntity(CreateTaskDTO dto) {
        Set<Category> categories = resolveCategoryIds(dto.getCategories());
        /* return new Task(
            dto.getTitle(),
            dto.getDescription(),
            dto.getDate(),
            Priority.valueOf(dto.getPriority()),
            categories
        ); */
        return Task.builder()
        .title(dto.getTitle())
        .description(dto.getDescription())
        .date(dto.getDate())
        .priority(Priority.valueOf(dto.getPriority()))
        .categories(categories)
        .build();
    }

    public Task updateEntity(UpdateTaskDTO dto, Task existingTask) {
        existingTask.setTitle(dto.getTitle());
        existingTask.setDescription(dto.getDescription());
        existingTask.setDate(dto.getDate());
        existingTask.setPriority(Priority.valueOf(dto.getPriority()));
        existingTask.setCategories(resolveCategoryIds(dto.getCategories()));
        existingTask.setCompleted(dto.isCompleted());
        return existingTask;
    
    }

    public Set<Category> resolveCategoryIds(Set<Long> ids){
        if(ids == null || ids.isEmpty()) return new HashSet<>();
        return categoryRepository.findByIdIn(ids);
    }
}
