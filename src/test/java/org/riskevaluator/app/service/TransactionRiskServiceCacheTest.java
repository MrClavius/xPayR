package org.riskevaluator.app.service;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.api.BinLookupApi;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.model.BinResource;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;
import org.riskevaluator.infra.adapter.in.rest.mapper.BinRiskProfileMapper;
import org.riskevaluator.infra.adapter.out.persistence.repository.RiskEvaluationLogRepository;
import org.riskevaluator.infra.adapter.out.persistence.mapper.RiskEvaluationLogMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@QuarkusTest
class TransactionRiskServiceCacheTest {

    @Inject
    TransactionRiskService service;

    @InjectMock
    @RestClient
    BinLookupApi binLookupApi;

    @InjectMock
    BinRiskProfileMapper binRiskProfileMapper;

    @InjectMock
    RuleProvider ruleProvider;

    @Inject
    RiskEvaluationLogRepository logRepository;

    @Inject
    RiskEvaluationLogMapper logMapper;

    Transaction transaction;

    @BeforeEach
    void setup() {
        transaction = Transaction.builder()
                .binNum(new BigDecimal("654321"))
                .amount(BigDecimal.TEN)
                .location("PL")
                .build();
    }

    @Test
    void shouldUseCacheForSameTransaction() {
        BinResource binResource = new BinResource().binNum("654321");
        BinRiskProfile profile = BinRiskProfile.builder().binNum("654321").build();

        reset(binLookupApi);
        when(binLookupApi.searchByAccountRangeResources(any())).thenReturn(List.of(binResource));
        when(binRiskProfileMapper.dtoToDomain(binResource)).thenReturn(profile);
        when(ruleProvider.provide()).thenReturn(List.of());

        var firstCall = service.calculateRisk(transaction);
        var secondCall = service.calculateRisk(transaction);

        assertThat(firstCall).isNotNull();
        assertThat(secondCall).isEqualTo(firstCall); // Should come from cache

        verify(binLookupApi, times(1)).searchByAccountRangeResources(any());
    }
}
