package toyproject.studyscheduler.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import toyproject.studyscheduler.common.response.ResponseCode;
import toyproject.studyscheduler.common.response.ResponseForm;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static toyproject.studyscheduler.common.response.ResponseCode.*;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        log.error("validation exception :: ", e);

        Map<String, String> errors = e.getBindingResult().getAllErrors().stream()
            .collect(Collectors.toMap(
                error -> ((FieldError) error).getField(),
                error -> error.getDefaultMessage()
            ));

        return ResponseEntity.badRequest()
            .body(ResponseForm.from(E90000, errors));
    }
}
