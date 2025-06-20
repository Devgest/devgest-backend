package tech.devgest.backend.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.devgest.backend.auth.dto.AuthResponse;
import tech.devgest.backend.auth.dto.LoginRequest;
import tech.devgest.backend.auth.dto.RegisterRequest;
import tech.devgest.backend.auth.service.AuthService;
import tech.devgest.backend.common.ApiRoutes;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRoutes.AUTH)
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(
            @Valid @RequestBody RegisterRequest request
    ) throws BadRequestException {
        log.info("Registration attempt for email: {}", request.getEmail());
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody LoginRequest request
    )  {
        log.info("Login attempt for email: {}", request.getEmail());
        AuthResponse authResponse = authService.authenticate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

}
