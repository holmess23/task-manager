package tasks.manager.event;

import java.util.logging.Logger;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AuditEventListener {
    private static final Logger logger = Logger.getLogger(AuditEventListener.class.getName());

    @EventListener
    public void onTaskCreated(TaskCreatedEvent event) {
        logger.info(String.format(
            "[AUDITORÍA] Tarea creada — ID: %d | Título: '%s' | Usuario: %s | Momento: %s",
            event.getTask().getId(),
            event.getTask().getTitle(),
            event.getUser().getEmail(),
            event.getCreatedAt()
        ));
    }

    @EventListener
    public void onTaskCompleted(TaskCompletedEvent event) {
        logger.info(String.format(
            "[AUDITORÍA] Tarea completada — ID: %d | Título: '%s' | Usuario: %s | Momento: %s",
            event.getTask().getId(),
            event.getTask().getTitle(),
            event.getUser().getEmail(),
            event.getCompletedAt()
        ));
    }
}
