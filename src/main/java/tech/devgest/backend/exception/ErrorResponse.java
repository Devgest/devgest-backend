package tech.devgest.backend.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
@Getter
public class ErrorResponse {
    private final Instant timestamp;
    private final int status;
    private final String message;
    private final String path;
    private final List<ErrorFieldDetail> fieldErrors;
}

record ErrorFieldDetail(
      String fieldName,
      String message
){}
