package dev.jessehaniel.blogapi.users;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final ModelMapper modelMapper;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> findAll() {
        return this.repository.findAll().stream()
            .map(element -> modelMapper.map(element, UserDto.class))
            .toList();
    }

    public User findByUsernameEntity(String username) {
        return this.repository.findByUsername(username).orElseThrow();
    }

    public UserDto findByUsername(String username) {
        User user = findByUsernameEntity(username);
        return this.modelMapper.map(user, UserDto.class);
    }

    public UserDto create(UserRequest userRequest) {
        log.info("New user: {}", userRequest);
        User user = this.modelMapper.map(userRequest, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = this.repository.save(user);
        return this.modelMapper.map(newUser, UserDto.class);
    }

    @Transactional
    public void deleteByUsername(String username) {
        log.info("Deleting user: {}", username);
        this.repository.deleteByUsername(username);
    }
}
