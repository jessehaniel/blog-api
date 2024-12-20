package dev.jessehaniel.blogapi.login;

import dev.jessehaniel.blogapi.security.jwt.JwtService;
import dev.jessehaniel.blogapi.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping
    public String login(@RequestBody AuthRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        this.authenticationManager.authenticate(authentication);
        var user = this.userRepository.findByUsername(request.username()).orElseThrow();
        return this.jwtService.createToken(user);
    }

}
