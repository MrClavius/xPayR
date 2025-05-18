package org.riskevaluator.domain.policy.rules;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;

import static org.assertj.core.api.Assertions.assertThat;

class LocalUseRuleTest {

    LocalUseRule rule = new LocalUseRule();

    @ParameterizedTest
    @CsvSource({
            "false, 15, 'Transaction is not local, but card is for local use'",
            "true, 0, ''",
    })
    void returnsExpectedScoreAndReason(Boolean localUse, int expectedScore, String expectedReason) {
        var tx = Transaction.builder().build();
        var bin = BinRiskProfile.builder()
                .localUse(localUse)
                .build();

        var match = rule.evaluate(tx, bin);

        assertThat(match.score()).isEqualTo(expectedScore);
        assertThat(match.rule()).isEqualTo("LocalUseRule");
        assertThat(match.reason()).isEqualTo(expectedReason);
    }

}