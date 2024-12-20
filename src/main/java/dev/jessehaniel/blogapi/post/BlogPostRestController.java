package dev.jessehaniel.blogapi.post;

import dev.jessehaniel.blogapi.login.PermissionValidation;
import dev.jessehaniel.blogapi.users.User;
import dev.jessehaniel.blogapi.users.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class BlogPostRestController {

    private final BlogPostService service;
    private final UserService userService;

    @GetMapping
    public List<BlogPostDto> findAll() {
        return this.service.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BlogPostDto create(@Valid @RequestBody BlogPostRequest blogPost, Authentication authentication) {
        User user = this.userService.findByUsernameEntity(authentication.getName());
        return this.service.create(blogPost, user);
    }

    @GetMapping("/{id}")
    public BlogPostDto findById(@PathVariable UUID id) {
        return this.service.findByUuid(id);
    }

    @PutMapping("/{id}")
    public BlogPostDto update(@PathVariable UUID id, @Valid @RequestBody BlogPostRequest blogPost, Authentication authentication) {
        BlogPost blogPostDb = this.service.findByUuidEntity(id);
        PermissionValidation.validate(blogPostDb.getAuthorName(), authentication);
        return this.service.update(blogPost, blogPostDb);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, Authentication authentication) {
        BlogPostDto blogPostDb = this.service.findByUuid(id);
        PermissionValidation.validate(blogPostDb.getAuthorUsername(), authentication);
        this.service.delete(id);
    }

}
