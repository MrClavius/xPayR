package org.riskevaluator.app.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.api.BinLookupApi;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.model.BinResource;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;
import org.riskevaluator.domain.policy.RuleMatch;
import org.riskevaluator.domain.policy.rules.AmountTransferRule;
import org.riskevaluator.infra.adapter.in.rest.mapper.BinRiskProfileMapper;
import org.riskevaluator.infra.adapter.out.persistence.entity.RiskEvaluationLogEntry;
import org.riskevaluator.infra.adapter.out.persistence.mapper.RiskEvaluationLogMapper;
import org.riskevaluator.infra.adapter.out.persistence.repository.RiskEvaluationLogRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionRiskServiceTest {

    @InjectMocks
    TransactionRiskService service;

    @Mock
    BinLookupApi binLookupApi;

    @Mock
    BinRiskProfileMapper binRiskProfileMapper;

    @Mock
    RiskEvaluationLogRepository logRepository;

    @Mock
    RiskEvaluationLogMapper logMapper;

    @Mock
    RuleProvider ruleProvider;

    Transaction transaction;

    @BeforeEach
    void setup() {
        transaction = Transaction.builder()
                .binNum(new BigDecimal("123456"))
                .amount(new BigDecimal("10000.0"))
                .location("PL")
                .build();
    }

    @Nested
    @DisplayName("When BIN data is found")
    class WhenBinFound {

        @Test
        void shouldEvaluateRiskAndPersist() {
            var binResource = new BinResource().binNum("123456");
            var profile = BinRiskProfile.builder().binNum("123456").build();
            var mapped = new RiskEvaluationLogEntry();

            when(binLookupApi.searchByAccountRangeResources(any()))
                    .thenReturn(List.of(binResource));
            when(binRiskProfileMapper.dtoToDomain(binResource)).thenReturn(profile);
            when(ruleProvider.provide()).thenReturn(List.of(new AmountTransferRule()));
            when(logMapper.toEntity(eq(transaction), eq(profile), any())).thenReturn(mapped);

            var result = service.calculateRisk(transaction);

            assertThat(result).isNotNull();
            assertThat(result.totalScore()).isZero();
            verify(logRepository).persist(mapped);
        }
    }

    @Nested
    @DisplayName("When BIN data is not found")
    class WhenBinNotFound {

        @Test
        void shouldReturnEmptyResultAndLog() {
            when(binLookupApi.searchByAccountRangeResources(any()))
                    .thenReturn(List.of());

            var result = service.calculateRisk(transaction);

            assertThat(result.totalScore()).isZero();
            assertThat(result.matches()).contains(RuleMatch.noBinInfo());
            verifyNoInteractions(binRiskProfileMapper, logRepository);
        }
    }
}