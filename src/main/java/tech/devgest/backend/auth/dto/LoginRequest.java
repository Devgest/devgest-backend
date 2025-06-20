package tech.devgest.backend.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class LoginRequest {

    private final String email;
    private final String password;

}
