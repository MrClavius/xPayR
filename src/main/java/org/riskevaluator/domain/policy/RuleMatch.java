package org.riskevaluator.domain.policy;

public record RuleMatch(String rule, double score, String reason) {
    public static RuleMatch noMatch(String rule) {
        return new RuleMatch(rule, 0.0, "");
    }

    public static RuleMatch match(String rule, double score, String reason) {
        return new RuleMatch(rule, score, reason);
    }

    public static RuleMatch noBinInfo() {
        return new RuleMatch("BIN_INFO", 0.0, "No BIN information available");
    }
}
