package org.riskevaluator.domain.policy;

import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;

public interface RiskRule {
    String name();
    int weight();
    RuleMatch evaluate(Transaction tx, BinRiskProfile bin);
}
