package org.riskevaluator.app.service;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.experimental.FieldDefaults;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.api.BinLookupApi;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.model.SearchByAccountRange;
import org.riskevaluator.app.port.in.TransactionRiskUseCase;
import org.riskevaluator.domain.model.Transaction;
import org.riskevaluator.domain.policy.*;
import org.riskevaluator.infra.adapter.in.rest.mapper.BinRiskProfileMapper;
import org.riskevaluator.infra.adapter.out.persistence.mapper.RiskEvaluationLogMapper;
import org.riskevaluator.infra.adapter.out.persistence.repository.RiskEvaluationLogRepository;
import org.riskevaluator.infra.cache.BinNumCacheKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TransactionRiskService implements TransactionRiskUseCase {

    private static final Logger logger = LoggerFactory.getLogger(TransactionRiskService.class);


    @Inject
    @RestClient
    BinLookupApi binLookupApi;

    @Inject
    BinRiskProfileMapper binRiskProfileMapper;

    @Inject
    RiskEvaluationLogRepository logRepository;

    @Inject
    RuleProvider ruleProvider;

    @Inject
    RiskEvaluationLogMapper logMapper;

    @Override
    @Transactional
    @CacheResult(cacheName = "riskScoringCache", keyGenerator = BinNumCacheKeyGenerator.class)
    public RiskResult calculateRisk(Transaction tx) {
        logger.info("Calculating risk for transaction: {}", tx);
        var bin = binLookupApi.searchByAccountRangeResources(
                new SearchByAccountRange().accountRange(tx.getBinNum()));

        if (bin.isEmpty()){
            logger.warn("No BIN found for transaction: {}", tx);
            return new RiskResult(0, "No BIN found for transaction", List.of(RuleMatch.noBinInfo()));
        }

        var binRiskProfile = binRiskProfileMapper.dtoToDomain(bin.getFirst());
        var rules = ruleProvider.provide();
        var result = new RiskScorer(rules).evaluate(tx, binRiskProfile);
        logRepository.persist(logMapper.toEntity(tx, binRiskProfile, result));
        return result;
    }
}