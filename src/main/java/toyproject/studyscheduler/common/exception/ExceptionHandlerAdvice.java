package toyproject.studyscheduler.common.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import toyproject.studyscheduler.common.response.ResponseForm;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static toyproject.studyscheduler.common.response.ResponseCode.*;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("spring validation exception :: ", e);

        List<String> errors = e.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .toList();

        return ResponseEntity.badRequest()
            .body(ResponseForm.from(E90000, errors));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        log.error("validation exception :: ", e);

        Map<String, String> errors = errorsToMap(e);

        return ResponseEntity.badRequest()
            .body(ResponseForm.from(E90000, errors));
    }

    private Map<String, String> errorsToMap(MethodArgumentNotValidException e) {
        return e.getBindingResult().getAllErrors().stream()
            .collect(Collectors.toMap(
                error -> getField(error),
                error -> error.getDefaultMessage()
            ));
    }

    private String getField(ObjectError error) {
        if (error instanceof FieldError) {
            return ((FieldError) error).getField();
        }

        return error.getObjectName();
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<?> handleCustomException(GlobalException e) {
        log.error("custom exception :: ", e);

        return ResponseEntity.badRequest()
            .body(ResponseForm.of(e.getResponseCode()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("argument type can't convert exception :: ", e);

        return ResponseEntity.badRequest()
            .body(ResponseForm.of(E90001));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("request param not exists exception :: ", e);

        return ResponseEntity.badRequest()
            .body(ResponseForm.of(E90002));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("path variable not exists exception :: ", e);

        return ResponseEntity.status(E90003.getHttpStatus())
            .body(ResponseForm.of(E90003));
    }
}
