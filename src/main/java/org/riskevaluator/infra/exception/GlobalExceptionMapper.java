package org.riskevaluator.infra.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.*;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

@Provider
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        logger.error("Unhandled exception occurred", exception);

        return switch (exception) {
            case NotFoundException e -> Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("NOT_FOUND", "The requested resource was not found", null))
                    .build();

            case ConstraintViolationException e -> {
                String details = e.getConstraintViolations().stream()
                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                        .collect(Collectors.joining(", "));
                yield Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("VALIDATION_ERROR", "Invalid input data", details))
                        .build();
            }

            case WebApplicationException e -> Response.status(e.getResponse().getStatus())
                    .entity(new ErrorResponse("REMOTE_SERVICE_ERROR", "An error occurred in an external service", e.getMessage()))
                    .build();

            default -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("INTERNAL_ERROR", "An unexpected internal error occurred", exception.getMessage()))
                    .build();
        };
    }
}