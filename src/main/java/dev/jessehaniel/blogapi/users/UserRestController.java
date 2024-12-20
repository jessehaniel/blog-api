package dev.jessehaniel.blogapi.users;

import dev.jessehaniel.blogapi.login.PermissionValidation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService service;

    @GetMapping
    @PreAuthorize("hasRole(T(dev.jessehaniel.blogapi.users.User.Role).ADMIN.name())")
    public List<UserDto> findAll() {
        return this.service.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserRequest user) {
        return this.service.create(user);
    }

    @GetMapping("/{username}")
    public UserDto findByUsername(@PathVariable String username, Authentication authentication) {
        PermissionValidation.validate(username, authentication);
        return this.service.findByUsername(username);
    }

    @DeleteMapping("/{username}")
    public void delete(@PathVariable String username, Authentication authentication) {
        PermissionValidation.validate(username, authentication);
        this.service.deleteByUsername(username);
    }

}
