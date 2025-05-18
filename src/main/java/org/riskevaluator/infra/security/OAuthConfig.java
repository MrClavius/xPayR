package org.riskevaluator.infra.security;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Getter
public class OAuthConfig {

    @ConfigProperty(name = "mc.consumer.key")
    String consumerKey;

    @ConfigProperty(name = "mc.private.key.path")
    String privateKeyPath;

    @ConfigProperty(name = "mc.private.key.alias")
    String privateKeyAlias;

    @ConfigProperty(name = "mc.private.key.password")
    String privateKeyPassword;
}