package tech.devgest.backend.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tech.devgest.backend.user.model.User;

@AllArgsConstructor
@Getter
@Setter
public class RegisterRequest {

    @NotNull
    private String name;

    @Email
    @NotNull
    @Size(max = 100)
    private String email;

    @NotNull
    @Size(min = 6)
    private String password;


    public User toUser(){
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }
}
