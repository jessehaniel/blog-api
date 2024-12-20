package dev.jessehaniel.blogapi.post.comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jessehaniel.blogapi.login.AuthRequest;
import dev.jessehaniel.blogapi.post.BlogPostDto;
import dev.jessehaniel.blogapi.post.BlogPostService;
import dev.jessehaniel.blogapi.users.UserService;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class CommentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private UserService userService;

    private UUID postId;

    @BeforeEach
    void setup() {
        this.postId = this.blogPostService.findAll().stream().findAny().map(BlogPostDto::getUuid).orElseThrow();
    }

    private String setupAuthToken(String username, String password) throws Exception {
        var authRequest = new AuthRequest(username, password);
        var response = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isOk())
            .andReturn();
        return response.getResponse().getContentAsString();
    }

    @Test
    @WithMockUser(username = "user1")
    void findAll() throws Exception {
        int sizeBefore = this.commentService.findAllByPostUuid(this.postId).size();
        var author = userService.findByUsernameEntity("user1");
        commentService.create(this.postId, new CommentRequest("Test Comment 1"), author);
        commentService.create(this.postId, new CommentRequest("Test Comment 2"), author);
        mockMvc.perform(get("/api/posts/{postId}/comments", this.postId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2+sizeBefore));
    }

    @Test
    void create() throws Exception {
        var author = userService.findByUsernameEntity("user1");
        var commentRequest = new CommentRequest("New Comment");
        String authToken = setupAuthToken("user1", "password1");
        var response = mockMvc.perform(post("/api/posts/{id}/comments", this.postId)
                .header("Authorization", "Bearer "+ authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        var commentDto = objectMapper.readValue(response.getResponse().getContentAsString(), CommentDto.class);
        assertThat(commentDto.getContent()).isEqualTo("New Comment");
        assertThat(commentDto.getAuthorUsername()).isEqualTo(author.getUsername());
    }

    @Test
    void findById() throws Exception {
        var author = userService.findByUsernameEntity("user1");
        var comment = commentService.create(postId, new CommentRequest("Comment Content"), author);
        String authToken = setupAuthToken("user1", "password1");

        mockMvc.perform(get("/api/posts/{id}/comments/{idComment}", postId, comment.getUuid())
                .header("Authorization", "Bearer "+ authToken))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.uuid").value(comment.getUuid().toString()))
            .andExpect(jsonPath("$.content").value("Comment Content"));
    }

    @Test
    void update() throws Exception {
        var author = userService.findByUsernameEntity("user1");
        var comment = commentService.create(postId, new CommentRequest("Original Content"), author);
        String authToken = setupAuthToken("user1", "password1");

        var updatedRequest = new CommentRequest("Updated Content");

        mockMvc.perform(put("/api/posts/{id}/comments/{idComment}", postId, comment.getUuid())
                .header("Authorization", "Bearer "+ authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content").value("Updated Content"));
    }

    @Test
    void unauthorizedUpdate() throws Exception {
        var author = userService.findByUsernameEntity("user1");
        var comment = commentService.create(postId, new CommentRequest("Original Content"), author);

        var updatedRequest = new CommentRequest("Malicious Update");

        mockMvc.perform(put("/api/posts/{id}/comments/{idComment}", postId, comment.getUuid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete() throws Exception {
        var author = userService.findByUsernameEntity("user1");
        var comment = commentService.create(postId, new CommentRequest("Content to Delete"), author);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}/comments/{idComment}", postId, comment.getUuid()))
            .andExpect(status().isNoContent());

        assertThatThrownBy(() -> commentService.findByUuidEntity(postId, comment.getUuid())).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void unauthorizedDelete() throws Exception {
        var author = userService.findByUsernameEntity("user1");
        var comment = commentService.create(postId, new CommentRequest("Content"), author);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}/comments/{idComment}", postId, comment.getUuid()))
            .andExpect(status().isForbidden());
    }
}
