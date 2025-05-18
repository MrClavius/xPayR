package org.riskevaluator.infra.exception;

public record ErrorResponse(String code, String message, String details) {}