package dev.jessehaniel.blogapi.post;

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
public class BlogPostService {

    private final BlogPostRepository repository;
    private final ModelMapper modelMapper;

    public List<BlogPostDto> findAll() {
        return this.repository.findAll().stream()
            .map(element -> modelMapper.map(element, BlogPostDto.class))
            .toList();
    }

    public BlogPostDto create(BlogPostRequest blogPost, User author) {
        BlogPost newBlogPost = this.modelMapper.map(blogPost, BlogPost.class);
        newBlogPost.setAuthor(author);
        log.info("New blog post: {}", blogPost);
        return this.modelMapper.map(this.repository.save(newBlogPost), BlogPostDto.class);
    }

    public BlogPost findByUuidEntity(UUID uuid) {
        return this.repository.findByUuid(uuid).orElseThrow();
    }

    public BlogPostDto findByUuid(UUID uuid) {
        return this.repository.findByUuid(uuid)
            .map(element -> modelMapper.map(element, BlogPostDto.class))
            .orElseThrow();
    }

    @Transactional
    public BlogPostDto update(BlogPostRequest blogPost, BlogPost blogPostDb) {
        BlogPost newBlogPost = this.modelMapper.map(blogPost, BlogPost.class);
        log.debug("Updating blog post: {}", blogPostDb.getId());
        log.debug("Changes on blog post: {}", blogPostDb.diff(newBlogPost));
        newBlogPost.setId(blogPostDb.getId());
        newBlogPost.setAuthor(blogPostDb.getAuthor());
        return this.modelMapper.map(repository.save(newBlogPost), BlogPostDto.class);
    }

    @Transactional
    public void delete(UUID id) {
        log.info("Deleting blog post: {}", id);
        this.repository.deleteByUuid(id);
    }
}
