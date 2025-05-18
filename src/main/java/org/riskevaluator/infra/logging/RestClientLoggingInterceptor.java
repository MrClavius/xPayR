package org.riskevaluator.infra.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.ext.Provider;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Provider
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RestClientLoggingInterceptor implements ClientRequestFilter, ClientResponseFilter {

    private static final Logger logger = LoggerFactory.getLogger(RestClientLoggingInterceptor.class);

    @Inject
    ObjectMapper mapper;

    @Override
    public void filter(ClientRequestContext requestContext) {
        try {
            var jsonPayload = requestContext.hasEntity()
                    ? mapper.writeValueAsString(requestContext.getEntity())
                    : "{}";

            var headersJsonMap = new HashMap<>(requestContext.getHeaders());

            var logLine = mapper.writeValueAsString(Map.of(
                    "type", "rest-client-request",
                    "method", requestContext.getMethod(),
                    "uri", requestContext.getUri().toString(),
                    "headers", headersJsonMap,
                    "payload", jsonPayload
            ));

            logger.info(logLine);

        } catch (Exception e) {
            logger.warn("Failed to log REST client request: " + e.getMessage());
        }
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
        try {
            var responseBody = responseContext.hasEntity()
                    ? mapper.writeValueAsString(requestContext.getEntity())
                    : "{}";

            var headersJsonMap = new HashMap<>(responseContext.getHeaders());

            var logLine = mapper.writeValueAsString(Map.of(
                    "type", "rest-client-response",
                    "uri", requestContext.getUri().toString(),
                    "status", responseContext.getStatus(),
                    "headers", headersJsonMap,
                    "body", responseBody != null ? responseBody : ""
            ));

            logger.info(logLine);

        } catch (Exception e) {
            logger.warn("Failed to log REST client response: " + e.getMessage());
        }
    }
}