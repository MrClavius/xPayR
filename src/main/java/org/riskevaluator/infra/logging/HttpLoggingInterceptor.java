package org.riskevaluator.infra.logging;

import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

@Provider
@Priority(1)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
class HttpLoggingInterceptor implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger logger = LoggerFactory.getLogger(HttpLoggingInterceptor.class);


    private static final String HEADER_NAME = "X-Request-Id";
    private static final String MDC_KEY = "requestId";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        var requestId = requestContext.getHeaderString(HEADER_NAME);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        MDC.put(MDC_KEY, requestId);
        requestContext.setProperty(MDC_KEY, requestId);

        logger.info("IN  [X-Request-Id={}] {} {}", requestId, requestContext.getMethod(), requestContext.getUriInfo().getRequestUri());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        var requestId = (String) requestContext.getProperty(MDC_KEY);
        if (requestId != null) {
            responseContext.getHeaders().add(HEADER_NAME, requestId);
        }

        logger.info("OUT [X-Request-Id={}] {}", requestId, responseContext.getStatus());

        MDC.remove(MDC_KEY);
    }
}