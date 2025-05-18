package org.riskevaluator.domain.policy.rules;

import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;
import org.riskevaluator.domain.policy.RiskRule;
import org.riskevaluator.domain.policy.RuleMatch;
import java.util.Map;
import java.util.Optional;

public class AnonymousPrepaidIndicatorRule implements RiskRule {

    private static final int MAX_WEIGHT = 20;

    private static final Map<String, IndicatorInfo> INDICATOR_MAP = Map.of(
            "A", new IndicatorInfo(20, "Anonymous prepaid program and not AMLDS compliant."),
            "E", new IndicatorInfo(20, "Anonymous prepaid program and AMLDS compliant."),
            "U", new IndicatorInfo(10, "Prepaid program is unknown."),
            "N", new IndicatorInfo(0,  "Not prepaid or non-anonymous prepaid.")
    );

    @Override
    public String name() {
        return "AnonymousPrepaidIndicatorRule";
    }

    @Override
    public int weight() {
        return MAX_WEIGHT;
    }

    @Override
    public RuleMatch evaluate(Transaction tx, BinRiskProfile bin) {
        String indicator = Optional.ofNullable(bin.getAnonymousPrepaidIndicator())
                .map(String::toUpperCase)
                .orElse(null);

        IndicatorInfo info = INDICATOR_MAP.get(indicator);
        if (info == null) {
            return RuleMatch.noMatch(name());
        }

        if (info.score() > 0) {
            return RuleMatch.match(name(), info.score(), info.reason());
        } else {
            return RuleMatch.noMatch(name());
        }
    }

    private record IndicatorInfo(int score, String reason) {}
}