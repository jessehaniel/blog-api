package dev.jessehaniel.blogapi.post.comment;

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
@RequestMapping("/api/posts/{id}/comments")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService service;
    private final UserService userService;

    @GetMapping
    public List<CommentDto> findAll(@PathVariable("id") UUID postId) {
        return this.service.findAllByPostUuid(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable("id") UUID postId, @Valid @RequestBody CommentRequest comment, Authentication authentication) {
        User user = this.userService.findByUsernameEntity(authentication.getName());
        return this.service.create(postId, comment, user);
    }

    @GetMapping("/{idComment}")
    public CommentDto findById(@PathVariable("id") UUID postId, @PathVariable("idComment") UUID id) {
        return this.service.findByUuid(postId, id);
    }

    @PutMapping("/{idComment}")
    public CommentDto update(@PathVariable("id") UUID postId, @PathVariable("idComment") UUID id, @Valid @RequestBody CommentRequest comment, Authentication authentication) {
        Comment commentDb = this.service.findByUuidEntity(postId, id);
        PermissionValidation.validate(commentDb.getAuthorName(), authentication);
        return this.service.update(postId, comment, commentDb);
    }

    @DeleteMapping("/{idComment}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID postId, @PathVariable("idComment") UUID id, Authentication authentication) {
        Comment commentDb = this.service.findByUuidEntity(postId, id);
        PermissionValidation.validate(commentDb.getAuthorName(), authentication);
        this.service.delete(id);
    }

}
