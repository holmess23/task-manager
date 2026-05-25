package tasks.manager.repository.tasks;

import org.springframework.data.jpa.domain.Specification;

import tasks.manager.dto.SearchCriteriaDTO;
import tasks.manager.model.task.Task;

public class SpecificationsBuilder {

    public static Specification<Task> build(SearchCriteriaDTO criteria) {
        Specification<Task> spec = TaskSpecifications.belongsToUser(criteria.getUser());

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
        return spec;
    }
    
}
