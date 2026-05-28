package tasks.manager.repository.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import tasks.manager.model.task.Task;
import tasks.manager.model.user.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    boolean existsByIdAndUser(Long id, User user);
    long countByUserId(Long id);
    
}
