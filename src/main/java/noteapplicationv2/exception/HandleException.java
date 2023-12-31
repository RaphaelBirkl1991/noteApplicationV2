package noteapplicationv2.exception;

import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import noteapplicationv2.domain.HttpResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static noteapplicationv2.util.DateUtil.dateTimeFormatter;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class HandleException extends ResponseEntityExceptionHandler implements ErrorController {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(exception.getMessage());
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason("Invalid fields: " + fieldsMessage)
                        .developerMessage(exception.getMessage())
                        .status((HttpStatus) status)
                        .statusCode(status.value())
                        .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(), status);
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, @Nullable Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason(exception.getMessage())
                        .developerMessage(exception.getMessage())
                        .status((HttpStatus) status)
                        .statusCode(status.value())
                        .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(), status);
            }

@ExceptionHandler(IllegalStateException.class)
 public ResponseEntity<HttpResponse<?>> illegalStateException(IllegalStateException exception){
    return createHttpErrorResponse(BAD_REQUEST, exception.getMessage(), exception);
 }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<HttpResponse<?>> noteNotFoundException(NoteNotFoundException exception){
        return createHttpErrorResponse(BAD_REQUEST, exception.getMessage(), exception);
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse<?>> noResultException(NoResultException exception){
        return createHttpErrorResponse(BAD_REQUEST, exception.getMessage(), exception);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<HttpResponse<?>> servletException(ServletException exception){
        return createHttpErrorResponse(BAD_REQUEST, exception.getMessage(), exception);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse<?>> exception(Exception exception){
        return createHttpErrorResponse(BAD_REQUEST, exception.getMessage(), exception);
    }

    private static ResponseEntity<HttpResponse<?>> createHttpErrorResponse(HttpStatus httpStatus, String reason, Exception exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason(reason)
                        .developerMessage(exception.getMessage())
                        .status((HttpStatus) httpStatus)
                        .statusCode(httpStatus.value())
                        .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(), httpStatus);
    }
}

