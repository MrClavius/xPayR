package org.riskevaluator.domain.policy.rules;

import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;
import org.riskevaluator.domain.policy.RiskRule;
import org.riskevaluator.domain.policy.RuleMatch;

import java.util.Optional;

public class CredentialStatusRule implements RiskRule {

    private static final String RULE_NAME = "CredentialStatusRule";
    private static final int RULE_WEIGHT = 15;
    private static final String REASON = "Card credential is not active.";
    private static final String ACTIVE_STATUS = "A";

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
        String status = Optional.ofNullable(bin.getCredentialStatus())
                .map(String::trim)
                .map(String::toUpperCase)
                .orElse(null);

        if (!ACTIVE_STATUS.equals(status)) {
            return RuleMatch.match(name(), weight(), REASON);
        }

        return RuleMatch.noMatch(name());
    }
}