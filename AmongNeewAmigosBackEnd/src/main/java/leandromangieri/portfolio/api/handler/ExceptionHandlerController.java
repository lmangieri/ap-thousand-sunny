package leandromangieri.portfolio.api.handler;

import com.amazonaws.services.cognitoidp.model.UsernameExistsException;
import leandromangieri.portfolio.core.errors.ObjectError;
import leandromangieri.portfolio.core.errors.ResponseErrorDto;
import leandromangieri.portfolio.core.errors.exceptions.BadRequestException;
import leandromangieri.portfolio.core.errors.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ResponseErrorDto handleBadRequestException(final BadRequestException ex) {
        return new ResponseErrorDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ResponseErrorDto handleBindException(final MethodArgumentNotValidException ex) {
        List<ObjectError> objectErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ObjectError(error.getDefaultMessage(), error.getField()))
                .collect(Collectors.toList());
        return new ResponseErrorDto(HttpStatus.BAD_REQUEST.value(), "Requisição possui campos inválidos", objectErrors);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected @ResponseBody ResponseErrorDto handleRuntimeException (final RuntimeException ex) {
        ex.printStackTrace();
        return new ResponseErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected @ResponseBody ResponseErrorDto handleConstraintViolationException(final ConstraintViolationException ex) {
        return new ResponseErrorDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
    }

    @ExceptionHandler(UsernameExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected @ResponseBody ResponseErrorDto handleUsernameExistsException(final UsernameExistsException ex) {
        return new ResponseErrorDto(HttpStatus.CONFLICT.value(), "User Already Exists", null);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected @ResponseBody ResponseErrorDto handleNotAuthorizedException(final UnauthorizedException ex) {
        return new ResponseErrorDto(HttpStatus.UNAUTHORIZED.value(), "Not Authorized", null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected @ResponseBody ResponseErrorDto handleAccessDeniedException(final AccessDeniedException ex) {
        return new ResponseErrorDto(HttpStatus.UNAUTHORIZED.value(), "Not Authorized", null);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected @ResponseBody ResponseErrorDto handleNotAuthorizedException(final NotAuthorizedException ex) {
        return new ResponseErrorDto(HttpStatus.UNAUTHORIZED.value(), "Wrong username or password", null);
    }
}
