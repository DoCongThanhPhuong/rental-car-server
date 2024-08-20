package net.codejava.utility;

import org.springframework.security.core.context.SecurityContextHolder;

import net.codejava.domain.entity.User;

public class AuthUtil {
    public static User getRequestedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
