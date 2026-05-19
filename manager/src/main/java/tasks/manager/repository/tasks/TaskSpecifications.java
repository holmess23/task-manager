package tasks.manager.repository.tasks;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import tasks.manager.model.task.Priority;
import tasks.manager.model.task.Task;
import tasks.manager.model.user.User;

public class TaskSpecifications {
    
    public static Specification<Task> belongsToUser(User user) {
        return ( Root<Task> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb) -> cb.equal(root.get("user"), user
        );
    }

    public static Specification<Task> hasCompleted(Boolean completed){
        return (root, query, cb) -> cb.equal(root.get("completed"), completed);
    }

    public static Specification<Task> titleContains(String text){
        return (root, query, cb) -> {
            String pattern = "%" + text.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("title")), pattern);
        };
    }

    public static Specification<Task> hasPriority(Priority priority){
        return (root, query, cb) -> cb.equal(root.get("priority"), priority);
    }

    public static Specification<Task> dueBefore(LocalDate date){
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("date"), date);
    }

    public static Specification<Task> dueAfter(LocalDate date){
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("date"), date);
    }

    public static Specification<Task> isOverdue(){
        return (root, query, cb) -> cb.and(cb.lessThan(root.get("date"), LocalDate.now()),
                                        cb.isFalse(root.get("completed")));
    }
}
