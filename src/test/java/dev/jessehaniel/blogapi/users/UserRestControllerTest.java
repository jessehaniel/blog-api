package dev.jessehaniel.blogapi.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jessehaniel.blogapi.login.AuthRequest;
import dev.jessehaniel.blogapi.users.User.Role;
import java.util.NoSuchElementException;
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
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private String validAuthToken;

    @BeforeEach
    void setupAuthToken() throws Exception {
        var username = "user1";
        var password = "password1";
        var authRequest = new AuthRequest(username, password);
        var response = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isOk())
            .andReturn();

        this.validAuthToken = response.getResponse().getContentAsString();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAll() throws Exception {
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findAllUnauthorized() throws Exception {
        mockMvc.perform(get("/users"))
            .andExpect(status().isForbidden());
    }

    @Test
    void create() throws Exception {
        var userRequest = new UserRequest("test@domain.com", "testuser", "password123", Role.COMMON_USER);
        var response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        var userDto = objectMapper.readValue(response.getResponse().getContentAsString(), UserDto.class);
        assertThat(userDto.getUsername()).isEqualTo("testuser");
        assertThat(userDto.getRole()).isEqualTo(Role.COMMON_USER);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findByUsername() throws Exception {
        var username = "testuser123";
        userService.create(new UserRequest("test123@domain.com", username, "password123", Role.COMMON_USER));
        mockMvc.perform(get("/users/{username}", username)
                .header("Authorization", validAuthToken))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    void findByUsernameUnauthorized() throws Exception {
        var username = "unauthorizedUser";
        mockMvc.perform(get("/users/{username}", username))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteAdmin() throws Exception {
        var username = "deletetest";
        userService.create(new UserRequest("delete@domain.com", username, "password123", Role.COMMON_USER));

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{username}", username)
                .header("Authorization", validAuthToken))
            .andExpect(status().isOk());

        assertThatThrownBy(() -> userService.findByUsername(username)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void deleteFail() throws Exception {
        var username = "deletetest";
        var userRequest = new UserRequest("delete@domain.com", username, "password123", Role.COMMON_USER);
        userService.create(userRequest);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{username}", username)
                .header("Authorization", validAuthToken))
            .andExpect(status().isForbidden());

        assertThat(userService.findByUsername(username)).isNotNull();
    }
}
