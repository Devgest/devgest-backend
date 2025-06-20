package tech.devgest.backend.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tech.devgest.backend.user.model.User;
import tech.devgest.backend.user.repository.UserRepository;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> registerOAuthUser(oauthUser));

        String token = jwtTokenUtil.generateToken(user);

        response.setContentType("application/json");
        response.getWriter().write(
            new ObjectMapper().writeValueAsString(
                Map.of("token", token)
            )
        );
        response.getWriter().flush();
    }

    private User registerOAuthUser(OAuth2User oauthUser) {
        User newUser = User.builder()
                .email(oauthUser.getAttribute("email"))
                .name(oauthUser.getAttribute("name"))
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .oAuth2Provider(OAuth2Provider.GITHUB)
                .build();
        return userRepository.save(newUser);
    }
}
