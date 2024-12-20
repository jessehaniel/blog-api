package dev.jessehaniel.blogapi.login;

import java.util.function.BiPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

public final class PermissionValidation {

    private PermissionValidation() { }

    public static final BiPredicate<Authentication, String> validatePermission = (auth, username) ->
        !auth.getName().equals(username) && auth.getAuthorities().stream()
            .noneMatch(it -> it.equals(new SimpleGrantedAuthority("ROLE_ADMIN")));

    public static void validate(String username, Authentication authentication) {
        if (validatePermission.test(authentication, username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
    }

}
