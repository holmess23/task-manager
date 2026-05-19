package tasks.manager.event;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import tasks.manager.model.task.Task;
import tasks.manager.model.user.User;

@Getter
public class TaskCreatedEvent extends ApplicationEvent{

    private final Task task;
    private final User user;
    private final LocalDateTime createdAt;

    public TaskCreatedEvent(Object source, Task task, User user) {
        super(source);
        this.task = task;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    
}
