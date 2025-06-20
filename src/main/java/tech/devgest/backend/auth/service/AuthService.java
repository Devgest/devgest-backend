package tech.devgest.backend.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.devgest.backend.auth.dto.AuthResponse;
import tech.devgest.backend.auth.dto.LoginRequest;
import tech.devgest.backend.auth.dto.RegisterRequest;
import tech.devgest.backend.auth.security.JwtTokenUtil;
import tech.devgest.backend.user.model.User;
import tech.devgest.backend.user.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;


    @Transactional
    public void register(RegisterRequest request) throws BadRequestException {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestException("Email Already Exists");
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = request.toUser();
        userRepository.save(user);
        log.info("new user created with ID : {}", user.getId());
    }

    @Transactional
    public AuthResponse authenticate(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        String token = jwtTokenUtil.generateToken(user);

        return new AuthResponse(token);
    }


}
