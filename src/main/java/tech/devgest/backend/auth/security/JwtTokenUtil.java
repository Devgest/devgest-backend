package tech.devgest.backend.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tech.devgest.backend.user.model.User;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenUtil {



    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5 hours

    @Value("${JWT_SECRET}")
    private String key;

    private final Base64.Decoder decoder = Base64.getDecoder();

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(decoder.decode(key));

        return JWT.create()
                .withIssuer("self")
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }

    public Boolean verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(decoder.decode(key));

        try{
            System.out.println(token);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

    public String getUsername(String token) {
        return JWT.decode(token).getSubject();
    }

    public String getClaimFromToken(String token, String claim) {
        return JWT.decode(token).getClaim(claim).asString();
    }

}