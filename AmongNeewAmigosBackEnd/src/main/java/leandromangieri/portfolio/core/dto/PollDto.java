package leandromangieri.portfolio.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class PollDto {
    @NotEmpty(message = "Título não pode ser vazio")
    private String title;

    @Size(min=2, message="Lista de opções precisa ser maior ou igual a 2")
    private List<String> options;
}
