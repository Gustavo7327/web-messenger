package br.com.web.messenger.dto.error;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
    Instant timestamp,
    Integer code,
    String status,
    List<String> errors
) {}

