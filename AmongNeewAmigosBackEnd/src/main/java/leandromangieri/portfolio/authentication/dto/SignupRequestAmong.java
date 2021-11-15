package leandromangieri.portfolio.authentication.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class SignupRequestAmong {
    @Email
    private String email;

    @NotEmpty(message = "password cannot be empty")
    @Size(min = 8, message = "Tamanho mínimo para o password são 8 caracteres")
    private String password;

    @NotEmpty(message = "nickName cannot be empty")
    private String nickName;
}
