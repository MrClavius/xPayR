package org.riskevaluator.domain.policy.rules;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;

import static org.assertj.core.api.Assertions.assertThat;

class GamblingBlockRuleTest {

    GamblingBlockRule rule = new GamblingBlockRule();

    @ParameterizedTest
    @CsvSource({
            "true, 15, 'Card is blocked for gambling: potential misuse pattern.'",
            "false, 0, ''",
    })
    void returnsExpectedScoreAndReason(Boolean gamblingBlockEnabled, int expectedScore, String expectedReason) {
        var tx = Transaction.builder().build();
        var bin = BinRiskProfile.builder()
                .gamblingBlockEnabled(gamblingBlockEnabled)
                .build();

        var match = rule.evaluate(tx, bin);

        assertThat(match.score()).isEqualTo(expectedScore);
        assertThat(match.rule()).isEqualTo("GamblingBlockRule");
        assertThat(match.reason()).isEqualTo(expectedReason);
    }
}