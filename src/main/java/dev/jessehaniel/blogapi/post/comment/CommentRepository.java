package dev.jessehaniel.blogapi.post.comment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBlogPost_Uuid(UUID blogPostUuid);
    Optional<Comment> findByUuid(UUID id);
    void deleteByUuid(UUID id);
}
