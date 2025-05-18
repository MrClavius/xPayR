package org.riskevaluator.domain.policy.rules;

import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;
import org.riskevaluator.domain.policy.RiskRule;
import org.riskevaluator.domain.policy.RuleMatch;

public class NonReloadableRule implements RiskRule {

    private static final String RULE_NAME = "NonReloadable";
    private static final int RULE_WEIGHT = 20;

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
        if (Boolean.TRUE.equals(bin.getNonReloadableIndicator())) {
            return RuleMatch.match(name(), weight(), "Card is non-reloadable.");
        }
        return RuleMatch.noMatch(name());
    }
}