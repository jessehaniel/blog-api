package dev.jessehaniel.blogapi.post;

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
import dev.jessehaniel.blogapi.users.User;
import dev.jessehaniel.blogapi.users.UserService;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class BlogPostRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private UserService userService;

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
    @WithMockUser(roles = "COMMON_USER")
    void findAll() throws Exception {
        mockMvc.perform(get("/api/posts"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "user1")
    void create() throws Exception {
        var blogPostRequest = new BlogPostRequest("Test Title", "Test Content");

        var response = mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blogPostRequest)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        var blogPostDto = objectMapper.readValue(response.getResponse().getContentAsString(), BlogPostDto.class);
        assertThat(blogPostDto.getTitle()).isEqualTo("Test Title");
        assertThat(blogPostDto.getAuthorUsername()).isEqualTo("user1");
    }

    @Test
    void findById() throws Exception {
        User user = this.userService.findByUsernameEntity("user1");
        var blogPost = blogPostService.create(
            new BlogPostRequest("Title for FindById", "Content for FindById"), user);
        String authToken = setupAuthToken(user.getUsername(), "password1");

        mockMvc.perform(get("/api/posts/{id}", blogPost.getUuid())
                .header("Authorization", "Bearer "+ authToken))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.uuid").value(blogPost.getUuid().toString()))
            .andExpect(jsonPath("$.title").value("Title for FindById"));
    }

    @Test
    void update() throws Exception {
        User user = this.userService.findByUsernameEntity("user1");
        var blogPost = blogPostService.create(new BlogPostRequest("Old Title", "Old Content"), user);
        var updatedRequest = new BlogPostRequest("Updated Title", "Updated Content");
        String authToken = setupAuthToken(user.getUsername(), "password1");

        mockMvc.perform(put("/api/posts/{id}", blogPost.getUuid())
                .header("Authorization", "Bearer "+ authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("Updated Title"))
            .andExpect(jsonPath("$.content").value("Updated Content"));
    }

    @Test
    @WithMockUser(roles = "COMMON_USER")
    void unauthorizedUpdate() throws Exception {
        User user = this.userService.findByUsernameEntity("user1");
        var blogPost = blogPostService.create(
            new BlogPostRequest("Title", "Content"), user);

        var updatedRequest = new BlogPostRequest("Malicious Title", "Malicious Content");
        mockMvc.perform(put("/api/posts/{id}", blogPost.getUuid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete() throws Exception {
        User user = this.userService.findByUsernameEntity("user1");
        var blogPost = blogPostService.create(new BlogPostRequest("Title to Delete", "Content to Delete"), user);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}", blogPost.getUuid()))
            .andExpect(status().isNoContent());

        assertThatThrownBy(() -> blogPostService.findByUuid(blogPost.getUuid())).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @WithMockUser(roles = "COMMON_USER")
    void unauthorizedDelete() throws Exception {
        User user = this.userService.findByUsernameEntity("user1");
        var blogPost = blogPostService.create(new BlogPostRequest("Title", "Content"), user);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{id}", blogPost.getUuid()))
            .andExpect(status().isForbidden());
    }
}
