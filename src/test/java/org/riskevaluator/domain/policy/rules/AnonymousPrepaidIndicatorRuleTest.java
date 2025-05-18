package org.riskevaluator.domain.policy.rules;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;

import static org.assertj.core.api.Assertions.assertThat;

class AnonymousPrepaidIndicatorRuleTest {

    AnonymousPrepaidIndicatorRule rule = new AnonymousPrepaidIndicatorRule();

    @ParameterizedTest
    @CsvSource({
            "A, 20, 'Anonymous prepaid program and not AMLDS compliant.'",
            "E, 20, 'Anonymous prepaid program and AMLDS compliant.'",
            "U, 10, 'Prepaid program is unknown.'",
            "N, 0, ''",
            "'', 0, ''",
    })
    void returnsExpectedScoreAndReason(String indicator, int expectedScore, String expectedReason) {
        var tx = Transaction.builder().build();
        var bin = BinRiskProfile.builder()
                .anonymousPrepaidIndicator("null".equals(indicator) ? null : indicator)
                .build();

        var match = rule.evaluate(tx, bin);

        assertThat(match.score()).isEqualTo(expectedScore);
        assertThat(match.rule()).isEqualTo("AnonymousPrepaidIndicatorRule");
        assertThat(match.reason()).isEqualTo(expectedReason);
    }
}