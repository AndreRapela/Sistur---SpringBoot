package br.gov.noronha.sistur.exception;

import br.gov.noronha.sistur.config.RequestIdFilter;
import br.gov.noronha.sistur.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage(), "RESOURCE_NOT_FOUND", request);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(NoSuchElementException ex, HttpServletRequest request) {
        String message = ex.getMessage() == null || ex.getMessage().isBlank()
            ? "Recurso não encontrado."
            : ex.getMessage();
        return error(HttpStatus.NOT_FOUND, message, "RESOURCE_NOT_FOUND", request);
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<ApiResponse<Object>> handleForbidden(ForbiddenOperationException ex, HttpServletRequest request) {
        return error(HttpStatus.FORBIDDEN, ex.getMessage(), "FORBIDDEN", request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleForbidden(AccessDeniedException ex, HttpServletRequest request) {
        return error(HttpStatus.FORBIDDEN, "Você não tem permissão para realizar esta ação.", "FORBIDDEN", request);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthenticated(UnauthenticatedException ex, HttpServletRequest request) {
        return error(HttpStatus.UNAUTHORIZED, ex.getMessage(), "UNAUTHENTICATED", request);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Object>> handleConflict(ConflictException ex, HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, ex.getMessage(), "CONFLICT", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConflict(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("Conflito de integridade. requestId={}", requestId(request));
        return error(HttpStatus.CONFLICT, "Os dados informados entram em conflito com um registro existente.", "DATA_CONFLICT", request);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Object>> handleResponseStatus(ResponseStatusException ex, HttpServletRequest request) {
        String reason = ex.getReason() == null ? "Não foi possível concluir a solicitação." : ex.getReason();
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return error(status, reason, responseStatusCode(status), request);
    }

    @ExceptionHandler({
        MissingServletRequestParameterException.class,
        MethodArgumentTypeMismatchException.class,
        HttpMessageNotReadableException.class,
        ConstraintViolationException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleInvalidRequest(Exception ex, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "Parâmetros da requisição inválidos.", "INVALID_REQUEST", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .distinct()
            .toList();
        return error(HttpStatus.BAD_REQUEST, "Dados inválidos: " + String.join(", ", errors), "VALIDATION_ERROR", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("Erro inesperado. requestId={}", requestId(request), ex);
        return error(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Erro interno no servidor. Tente novamente mais tarde.",
            "INTERNAL_ERROR",
            request
        );
    }

    private ResponseEntity<ApiResponse<Object>> error(
        HttpStatus status,
        String message,
        String code,
        HttpServletRequest request
    ) {
        return ResponseEntity.status(status).body(ApiResponse.error(message, code, requestId(request)));
    }

    private String requestId(HttpServletRequest request) {
        Object value = request.getAttribute(RequestIdFilter.ATTRIBUTE);
        return value == null ? null : value.toString();
    }

    private String responseStatusCode(HttpStatus status) {
        return switch (status) {
            case BAD_REQUEST -> "INVALID_REQUEST";
            case UNAUTHORIZED -> "UNAUTHENTICATED";
            case FORBIDDEN -> "FORBIDDEN";
            case NOT_FOUND -> "RESOURCE_NOT_FOUND";
            case CONFLICT -> "CONFLICT";
            case PAYLOAD_TOO_LARGE -> "PAYLOAD_TOO_LARGE";
            case TOO_MANY_REQUESTS -> "RATE_LIMITED";
            default -> status.is5xxServerError() ? "UPSTREAM_ERROR" : "HTTP_ERROR";
        };
    }
}
