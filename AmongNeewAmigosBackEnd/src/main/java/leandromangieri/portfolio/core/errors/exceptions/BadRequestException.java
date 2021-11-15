package leandromangieri.portfolio.core.errors.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {
    private HttpStatus httpStatus;

    public BadRequestException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }
}
