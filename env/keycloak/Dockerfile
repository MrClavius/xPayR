FROM quay.io/keycloak/keycloak:24.0.3

WORKDIR /opt/keycloak

COPY --chown=keycloak:keycloak realm /opt/keycloak/data/import

EXPOSE 8080

ENTRYPOINT ["/bin/bash", "-c", "/opt/keycloak/bin/kc.sh start-dev --import-realm"]