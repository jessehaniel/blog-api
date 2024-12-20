package dev.jessehaniel.blogapi.users;

import dev.jessehaniel.blogapi.users.User.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRequest {

    @Email
    private String email;
    @Pattern(regexp = "[\\w.]{5,20}", message = "Username must be alphanumeric between 5 and 20 characters (lowercase, uppercase, numbers, _, .)")
    private String username;
    private String password;
    @NotNull
    private Role role;

}
