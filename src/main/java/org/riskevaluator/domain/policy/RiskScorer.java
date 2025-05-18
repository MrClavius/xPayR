package org.riskevaluator.domain.policy;

import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class RiskScorer {

    private final List<RiskRule> rules;

    public RiskScorer(List<RiskRule> rules) {
        this.rules = rules;
    }

    public RiskResult evaluate(Transaction tx, BinRiskProfile bin) {
        var matches = rules.stream()
                .map(rule -> rule.evaluate(tx, bin))
                .collect(Collectors.toList());

        var rawTotal = matches.stream().mapToDouble(RuleMatch::score).sum();
        var maxPossible = rules.stream().mapToInt(RiskRule::weight).sum();

        var normalizedScore = maxPossible == 0 ? 0 : Math.round((rawTotal * 100) / maxPossible);

        var message = generateRiskMessage(normalizedScore);

        return new RiskResult(normalizedScore, message, matches);
    }

    private String generateRiskMessage(double score) {
        if (score <= 30) {
            return "Risk score " + score + " (low): Low risk of fraud. Further reason below.";
        } else if (score <= 60) {
            return "Risk score " + score + " (medium): Medium risk of fraud. Further reason below.";
        } else if (score <= 90) {
            return "Risk score " + score + " (high): High risk of fraud. Further reason below.";
        } else {
            return "Risk score " + score + " (very high): Very high risk of fraud. Further reason below.";
        }
    }
}
