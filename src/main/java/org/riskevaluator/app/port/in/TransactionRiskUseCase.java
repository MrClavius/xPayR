package org.riskevaluator.app.port.in;

import org.riskevaluator.domain.model.Transaction;
import org.riskevaluator.domain.policy.RiskResult;

public interface TransactionRiskUseCase {
    RiskResult calculateRisk(Transaction transaction);
}