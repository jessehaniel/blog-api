package dev.jessehaniel.blogapi.users;

import dev.jessehaniel.blogapi.users.User.Role;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto implements Serializable {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private boolean deleted;
    private String username;
    private String password;
    private String email;
    private String displayName;
    private LocalDate dob;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean credentialsExpired;
    private Role role;
}
