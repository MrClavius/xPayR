package org.riskevaluator.app.service;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import lombok.experimental.FieldDefaults;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.api.BinLookupApi;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.model.SearchByAccountRange;
import org.riskevaluator.app.port.in.BinLookupUseCase;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.infra.adapter.in.rest.mapper.BinRiskProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BinLookupService implements BinLookupUseCase {

    private static final Logger logger = LoggerFactory.getLogger(BinLookupService.class);

    @Inject
    @RestClient
    BinLookupApi binLookupApi;

    @Inject
    BinRiskProfileMapper binRiskProfileMapper;

    @Override
    @CacheResult(cacheName = "binCache")
    public BinRiskProfile getBin(@CacheKey SearchByAccountRange searchByAccountRange) {
        logger.info("Searching BIN with account range: {}", searchByAccountRange);
        var fromApi = binLookupApi.searchByAccountRangeResources(searchByAccountRange);
        if (fromApi.isEmpty()) throw new NotFoundException("No BIN found for account range: " + searchByAccountRange);
        return binRiskProfileMapper.dtoToDomain(fromApi.getFirst());
    }
}
