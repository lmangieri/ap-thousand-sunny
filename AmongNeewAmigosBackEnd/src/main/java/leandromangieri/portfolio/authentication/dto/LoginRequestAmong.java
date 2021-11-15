package leandromangieri.portfolio.authentication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class LoginRequestAmong {
    @Email
    @NotEmpty(message = "email cannot be empty")
    private String email;

    @NotEmpty(message = "password cannot be empty")
    @Size(min = 8, message = "Tamanho mínimo para o password são 8 caracteres")
    private String password;
}