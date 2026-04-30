package br.gov.noronha.sistur.exception;

import br.gov.noronha.sistur.dto.ApiResponse;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex) {
        // Em produção, não expor stacktrace
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erro interno no servidor. Por favor, tente novamente mais tarde."));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage();

        if (message == null || message.isBlank()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro interno no servidor. Por favor, tente novamente mais tarde."));
        }

        String normalized = message.toLowerCase(Locale.ROOT);
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (normalized.contains("not found") || normalized.contains("não encontrado") || normalized.contains("não encontrada")) {
            status = HttpStatus.NOT_FOUND;
        } else if (normalized.contains("permission denied") || normalized.contains("sem permissão") || normalized.contains("acesso negado")) {
            status = HttpStatus.FORBIDDEN;
        }

        return ResponseEntity.status(status)
                .body(ApiResponse.error(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erros de validação: " + String.join(", ", errors)));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFoundException(NoSuchElementException ex) {
        String message = ex.getMessage();
        if (message == null || message.isBlank()) {
            message = "Recurso não encontrado.";
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(message));
    }
}
