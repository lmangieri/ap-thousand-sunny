package leandromangieri.portfolio.core.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResponseErrorDto {

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    private int code;

    private String message;
    private List<ObjectError> erros;

    public ResponseErrorDto(int code, String message, List<ObjectError> errors) {
        this.timestamp = LocalDateTime.now();
        this.code = code;
        this.message = message;
        this.erros = errors;
    }
}
