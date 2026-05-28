package tasks.manager.exception;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
        MethodArgumentNotValidException ex){
            Map<String, List<String>> fieldErrors = ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .collect(Collectors.groupingBy(
                                error -> error.getField(),
                                Collectors.mapping(
                                    error -> error.getDefaultMessage(),
                                    Collectors.toList()
                                )
                            ));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiError.ofValidation(fieldErrors));
        }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleMalformedJson(
        HttpMessageNotReadableException ex){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiError.of(400, "El cuerpo de la petición es inválido o está ausente."));
        }
    

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiError> handleTaskNotFoundException(TaskNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiError.of(404, ex.getMessage()));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiError> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiError.of(404, ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(400, ex.getMessage()));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiError> handleDisabledException(DisabledException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiError.of(403, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        String message = String.format(
            "El parámetro '%s' debe ser de tipo %s",
            ex.getName(),
            ex.getRequiredType() != null 
                ? ex.getRequiredType().getSimpleName() 
                : "desconocido"
        );
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError.of(400, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception ex) {
        ex.printStackTrace();
        System.out.println("Tipo de excepción: " + ex.getClass().getName());
        System.out.println("Mensaje de excepción: " + ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(500, "Error interno del servidor."));
    }


}
