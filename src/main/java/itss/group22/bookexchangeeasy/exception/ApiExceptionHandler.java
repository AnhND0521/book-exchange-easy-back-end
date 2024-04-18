package itss.group22.bookexchangeeasy.exception;

import itss.group22.bookexchangeeasy.dto.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorMessage> apiException(ApiException exception, WebRequest webRequest) {
        return new ResponseEntity<>(new ErrorMessage(exception.getMessage()), exception.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> accessDeniedException(AccessDeniedException exception, WebRequest webRequest) {
        return new ResponseEntity<>(new ErrorMessage(exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> illegalArgumentException(IllegalArgumentException exception, WebRequest webRequest) {
        if (exception.getMessage().contains("No enum constant"))
            return new ResponseEntity<>(new ErrorMessage(exception.getMessage()), HttpStatus.BAD_REQUEST);
        return globalException(exception, webRequest);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest webRequest) {
        if (exception.getMessage().contains("Validation failed"))
            return new ResponseEntity<>(new ErrorMessage(exception.getMessage()), HttpStatus.BAD_REQUEST);
        return globalException(exception, webRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalException(Exception exception, WebRequest webRequest) {
        exception.printStackTrace();
        return new ResponseEntity<>(new ErrorMessage(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
