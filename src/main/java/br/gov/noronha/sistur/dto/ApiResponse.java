package br.gov.noronha.sistur.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String code;
    private String requestId;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(message, "REQUEST_ERROR", null);
    }

    public static <T> ApiResponse<T> error(String message, String code, String requestId) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .code(code)
                .requestId(requestId)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
