package org.riskevaluator.domain.policy.rules;

import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;
import org.riskevaluator.domain.policy.RiskRule;
import org.riskevaluator.domain.policy.RuleMatch;

import java.math.BigDecimal;
import java.util.List;

public class AmountTransferRule implements RiskRule {

    private static final List<Threshold> THRESHOLDS = List.of(
            new Threshold(new BigDecimal("500000"), 20, "Transaction amount above 500000."),
            new Threshold(new BigDecimal("250000"), 15, "Transaction amount above 250000."),
            new Threshold(new BigDecimal("100000"), 10, "Transaction amount above 100000."),
            new Threshold(new BigDecimal("10000"), 5, "Transaction amount above 10000.")
    );

    @Override
    public String name() {
        return "AmountTransferRule";
    }

    @Override
    public int weight() {
        return 20;
    }

    @Override
    public RuleMatch evaluate(Transaction tx, BinRiskProfile bin) {
        BigDecimal amount = tx.getAmount();

        if (amount == null || amount.compareTo(THRESHOLDS.getLast().amount()) <= 0) {
            return RuleMatch.noMatch(name());
        }

        for (Threshold threshold : THRESHOLDS) {
            if (amount.compareTo(threshold.amount()) > 0) {
                return RuleMatch.match(name(), threshold.score(), threshold.reason());
            }
        }

        return RuleMatch.noMatch(name());
    }

    private record Threshold(BigDecimal amount, int score, String reason) {
    }
}