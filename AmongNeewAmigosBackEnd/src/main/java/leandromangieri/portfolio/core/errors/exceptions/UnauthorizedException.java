package leandromangieri.portfolio.core.errors.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends RuntimeException{
    private HttpStatus httpStatus;

    public UnauthorizedException(String message) {
        super(message);
        this.httpStatus = HttpStatus.FORBIDDEN;
    }
}
