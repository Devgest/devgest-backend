package tech.devgest.backend.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.devgest.backend.user.model.User;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class AuthTokenFilter extends OncePerRequestFilter {


    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailService userDetailService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String jwt = request.getHeader("Authorization");

        if (
                jwt != null &&
                jwt.startsWith("Bearer ") &&
                jwtTokenUtil.verifyToken(jwt.substring(7)) &&
                SecurityContextHolder.getContext().getAuthentication() == null
        ) {
            try {
                String token = jwt.substring(7);
                String email = jwtTokenUtil.getUsername(token);
                User userDetails = (User) userDetailService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (UsernameNotFoundException e) {
                //TODO: add log
                System.out.println("UsernameNotFoundException");
            }
        }
        filterChain.doFilter(request, response);
    }
}
