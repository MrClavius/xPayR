package org.riskevaluator.domain.policy.rules;

import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;
import org.riskevaluator.domain.policy.RiskRule;
import org.riskevaluator.domain.policy.RuleMatch;

import java.util.Optional;

public class CountryMismatchRule implements RiskRule {

    private static final String RULE_NAME = "CountryMismatch";
    private static final int RULE_WEIGHT = 30;
    private static final String REASON = "Transaction country differs from card issuer.";

    @Override
    public String name() {
        return RULE_NAME;
    }

    @Override
    public int weight() {
        return RULE_WEIGHT;
    }

    @Override
    public RuleMatch evaluate(Transaction tx, BinRiskProfile bin) {
        var binCountry = Optional.ofNullable(bin.getCountry())
                .map(String::trim)
                .map(String::toUpperCase)
                .orElse(null);

        var txLocation = Optional.ofNullable(tx.getLocation())
                .map(String::trim)
                .map(String::toUpperCase)
                .orElse(null);

        if (binCountry != null && txLocation != null && !binCountry.equals(txLocation)) {
            return RuleMatch.match(name(), weight(), REASON);
        }

        return RuleMatch.noMatch(name());
    }
}