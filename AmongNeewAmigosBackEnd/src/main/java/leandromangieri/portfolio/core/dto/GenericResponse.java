package leandromangieri.portfolio.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class GenericResponse {
    public GenericResponse() {
    }
    public GenericResponse(String result) {
        this.result = result;
    }
    private String result;
}
