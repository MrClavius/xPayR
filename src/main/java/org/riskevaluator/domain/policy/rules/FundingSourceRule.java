package org.riskevaluator.domain.policy.rules;

import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;
import org.riskevaluator.domain.policy.RiskRule;
import org.riskevaluator.domain.policy.RuleMatch;

import java.util.Map;
import java.util.Optional;

public class FundingSourceRule implements RiskRule {

    private static final String RULE_NAME = "FundingSourceRule";
    private static final int MAX_WEIGHT = 20;

    private static final Map<String, Integer> SCORES = Map.of(
            "CREDIT", 0,
            "DEBIT", 10,
            "PREPAID", 20
    );

    private static final Map<String, String> REASONS = Map.of(
            "CREDIT", "Credit card: lower fraud risk.",
            "DEBIT", "Debit card: moderate fraud risk.",
            "PREPAID", "Prepaid card: higher fraud risk."
    );

    @Override
    public String name() {
        return RULE_NAME;
    }

    @Override
    public int weight() {
        return MAX_WEIGHT;
    }

    @Override
    public RuleMatch evaluate(Transaction tx, BinRiskProfile bin) {
        String fundingSource = Optional.ofNullable(bin.getFundingSource())
                .map(String::trim)
                .map(String::toUpperCase)
                .orElse(null);

        if (fundingSource == null || !SCORES.containsKey(fundingSource)) {
            return RuleMatch.noMatch(name());
        }

        int score = SCORES.get(fundingSource);
        String reason = REASONS.getOrDefault(fundingSource, "Unknown funding source.");

        if (score <= 0) {
            return RuleMatch.noMatch(name());
        }

        return RuleMatch.match(name(), score, reason);
    }
}