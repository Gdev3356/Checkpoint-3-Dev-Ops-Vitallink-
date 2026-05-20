package br.com.fiap.vitallink.exceptions;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        List<String> mensagens,
        String path
) {}
