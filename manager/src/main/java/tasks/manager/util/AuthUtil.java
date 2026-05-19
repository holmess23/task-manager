package tasks.manager.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import tasks.manager.model.user.User;

@Component
public class AuthUtil {
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
    }
}
