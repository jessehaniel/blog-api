package dev.jessehaniel.blogapi.post.comment;

import dev.jessehaniel.blogapi.post.BlogPost;
import dev.jessehaniel.blogapi.post.BlogPostService;
import dev.jessehaniel.blogapi.users.User;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository repository;
    private final BlogPostService blogPostService;
    private final ModelMapper modelMapper;

    public List<CommentDto> findAllByPostUuid(UUID postUuid) {
        return this.repository.findAllByBlogPost_Uuid(postUuid).stream()
            .map(element -> modelMapper.map(element, CommentDto.class))
            .toList();
    }

    @Transactional
    public CommentDto create(UUID postId, CommentRequest comment, User author) {
        Comment newComment = this.modelMapper.map(comment, Comment.class);
        BlogPost blogPost = blogPostService.findByUuidEntity(postId);
        newComment.setBlogPost(blogPost);
        newComment.setAuthor(author);
        log.info("New comment for blog post uuid ({}): {}", postId, newComment);
        return this.modelMapper.map(this.repository.save(newComment), CommentDto.class);
    }

    public Comment findByUuidEntity(UUID postId, UUID id) {
        Comment comment = this.repository.findByUuid(id).orElseThrow();
        validateBlogPostComment(postId, comment);
        return comment;
    }

    public CommentDto findByUuid(UUID postId, UUID id) {
        Comment comment = findByUuidEntity(postId, id);
        return this.modelMapper.map(comment, CommentDto.class);
    }

    private static void validateBlogPostComment(UUID postId, Comment comment) {
        if (!comment.getBlogPost().getUuid().equals(postId)) {
            throw new IllegalArgumentException("Comment id doesn't match post id");
        }
    }

    @Transactional
    public CommentDto update(UUID postId, CommentRequest comment, Comment commentDb) {
        Comment newComment = this.modelMapper.map(comment, Comment.class);
        validateBlogPostComment(postId, commentDb);
        log.debug("Updating comment for blog post uuid ({}): {}", postId, commentDb);
        log.debug("Changes on comment: {}", commentDb.diff(newComment));
        newComment.setId(commentDb.getId());
        newComment.setUuid(commentDb.getUuid());
        newComment.setBlogPost(commentDb.getBlogPost());
        newComment.setAuthor(commentDb.getAuthor());
        return this.modelMapper.map(this.repository.save(newComment), CommentDto.class);
    }

    @Transactional
    public void delete(UUID id) {
        log.info("Deleting comment: {}", id);
        this.repository.deleteByUuid(id);
    }
}
