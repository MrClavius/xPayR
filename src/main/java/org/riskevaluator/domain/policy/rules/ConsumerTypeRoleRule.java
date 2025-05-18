package org.riskevaluator.domain.policy.rules;

import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;
import org.riskevaluator.domain.policy.RiskRule;
import org.riskevaluator.domain.policy.RuleMatch;

import java.util.Optional;

public class ConsumerTypeRoleRule implements RiskRule {

    private static final int RULE_WEIGHT = 20;
    private static final String CONSUMER_TYPE = "CONSUMER";
    private static final String REASON_CONSUMER = "Consumer card: higher fraud risk.";

    @Override
    public String name() {
        return "ConsumerTypeRoleRule";
    }

    @Override
    public int weight() {
        return RULE_WEIGHT;
    }

    @Override
    public RuleMatch evaluate(Transaction tx, BinRiskProfile bin) {
        var type = Optional.ofNullable(bin.getConsumerType())
                .map(String::toUpperCase)
                .orElse(null);

        if (type == null) {
            return RuleMatch.noMatch(name());
        }

        if (CONSUMER_TYPE.equals(type)) return RuleMatch.match(name(), weight(), REASON_CONSUMER);

        return RuleMatch.noMatch(name());
    }
}
