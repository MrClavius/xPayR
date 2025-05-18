package org.riskevaluator.domain.policy;

import java.util.List;

public record RiskResult(double totalScore, String message, List<RuleMatch> matches) {}
