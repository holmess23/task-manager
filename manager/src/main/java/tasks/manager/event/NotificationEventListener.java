package tasks.manager.event;

import java.util.logging.Logger;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {
    private static final Logger logger = Logger.getLogger(NotificationEventListener.class.getName());

    @EventListener
    @Async
    public void onTaskCreated(TaskCreatedEvent event) {
        logger.info(String.format(
            "[NOTIFICACIÓN] Enviando notificación a %s: '¡Has completado la tarea: %s!'",
            event.getUser().getEmail(),
            event.getTask().getTitle()
        ));

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        logger.info(String.format(
            "[NOTIFICACIÓN] Notificación enviada a %s: '¡Has completado la tarea: %s!'",
            event.getUser().getEmail(),
            event.getTask().getTitle()
        ));
    }
}
