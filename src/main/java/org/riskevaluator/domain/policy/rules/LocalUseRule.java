package org.riskevaluator.domain.policy.rules;

import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;
import org.riskevaluator.domain.policy.RiskRule;
import org.riskevaluator.domain.policy.RuleMatch;

public class LocalUseRule implements RiskRule {

    private static final String RULE_NAME = "LocalUseRule";
    private static final int RULE_WEIGHT = 15;

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
        if (Boolean.FALSE.equals(bin.getLocalUse())) {
            return RuleMatch.match(name(), weight(),
                    "Transaction is not local, but card is for local use");
        }
        return RuleMatch.noMatch(name());
    }
}