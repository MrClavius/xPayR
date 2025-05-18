package org.riskevaluator.domain.policy.rules;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;

import static org.assertj.core.api.Assertions.assertThat;

class NonReloadableRuleTest {
    NonReloadableRule rule = new NonReloadableRule();

    @ParameterizedTest
    @CsvSource({
            "true, 20, 'Card is non-reloadable.'",
            "false, 0, ''",
    })
    void returnsExpectedScoreAndReason(Boolean nonReloadableIndicator, int expectedScore, String expectedReason) {
        var tx = Transaction.builder().build();
        var bin = BinRiskProfile.builder()
                .nonReloadableIndicator(nonReloadableIndicator)
                .build();

        var match = rule.evaluate(tx, bin);

        assertThat(match.score()).isEqualTo(expectedScore);
        assertThat(match.rule()).isEqualTo("NonReloadable");
        assertThat(match.reason()).isEqualTo(expectedReason);
    }

}