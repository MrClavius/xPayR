package org.riskevaluator.infra.adapter.out.persistence.mapper;

import org.jboss.logging.MDC;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;
import org.riskevaluator.domain.policy.RiskResult;
import org.riskevaluator.infra.adapter.out.persistence.entity.RiskEvaluationLogEntry;

import java.time.Instant;

@Mapper(componentModel = "cdi", imports = {Instant.class, MDC.class})
public interface RiskEvaluationLogMapper {

    @Mapping(target = "requestId", expression = "java(String.valueOf(MDC.get(\"requestId\")))")
    @Mapping(target = "timestamp", expression = "java(Instant.now())")
    @Mapping(target = "binNum", source = "tx.binNum")
    @Mapping(target = "amount", source = "tx.amount")
    @Mapping(target = "merchantLocation", source = "tx.location")
    @Mapping(target = "cardCountry", source = "bin.country")
    @Mapping(target = "isLocalUse", expression = "java(Boolean.TRUE.equals(bin.getLocalUse()))")
    @Mapping(target = "isReloadable", expression = "java(Boolean.FALSE.equals(bin.getNonReloadableIndicator()))")
    @Mapping(target = "matchedRules", expression = "java(result.matches().stream().map(org.riskevaluator.domain.policy.RuleMatch::rule).toList())")
    @Mapping(target = "riskScore", source = "result.totalScore")
    @Mapping(target = "finalDecision", expression = "java(decide(result.totalScore()))")
    RiskEvaluationLogEntry toEntity(Transaction tx, BinRiskProfile bin, RiskResult result);

    default String decide(double score) {
        if (score >= 80.0) return "High Risk";
        if (score >= 50.0) return "Medium Risk";
        return "Low Risk";
    }
}
