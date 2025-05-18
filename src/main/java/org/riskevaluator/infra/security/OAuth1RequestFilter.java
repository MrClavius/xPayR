package org.riskevaluator.infra.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercard.developer.oauth.OAuth;
import com.mastercard.developer.utils.AuthenticationUtils;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.riskevaluator.infra.exception.KeyLoadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

@Provider
@ApplicationScoped
@Priority(Priorities.AUTHENTICATION)
public class OAuth1RequestFilter implements ClientRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(OAuth1RequestFilter.class);

    @Inject
    OAuthConfig config;

    @Inject
    ObjectMapper objectMapper;

    PrivateKey signingKey;

    @Override
    public void filter(ClientRequestContext requestContext) throws JsonProcessingException {
        logger.info("config: {}", objectMapper.writeValueAsString(config));
        try {
            if (signingKey == null) loadKey();
        } catch (KeyLoadException e) {
            logger.error("Error loading private key", e);
            throw new WebApplicationException("Unable to load signing key", 500);
        }

        var uri = requestContext.getUri();
        var method = requestContext.getMethod();
        var body = objectMapper.writeValueAsString(requestContext.getEntity());

        var authHeader = OAuth.getAuthorizationHeader(
                uri,
                method,
                body,
                StandardCharsets.UTF_8,
                config.getConsumerKey(),
                signingKey
        );

        requestContext.getHeaders().add("Authorization", authHeader);
    }

    private void loadKey() throws KeyLoadException {
        try (var keyStream = getClass().getResourceAsStream(config.getPrivateKeyPath())) {
            if (keyStream == null) {
                throw new KeyLoadException("Private key not found at path: " + config.getPrivateKeyPath());
            }
            signingKey = AuthenticationUtils.loadSigningKey(keyStream, config.getPrivateKeyAlias(), config.getPrivateKeyPassword());
        } catch (Exception e) {
            throw new KeyLoadException("Could not load private key", e);
        }
    }
}