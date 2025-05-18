package org.riskevaluator.domain.policy.rules;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;

import static org.assertj.core.api.Assertions.assertThat;

class CredentialStatusRuleTest {

    CredentialStatusRule rule = new CredentialStatusRule();

    @ParameterizedTest
    @CsvSource({
            "A, 0, ''",
            "a, 0, ''",
            "B, 15, 'Card credential is not active.'",
            "'', 15, 'Card credential is not active.'",
            "null, 15, 'Card credential is not active.'"
    })
    void returnsExpectedScoreAndReason(String credentialStatus, int expectedScore, String expectedReason) {
        var tx = Transaction.builder().build();
        var bin = BinRiskProfile.builder()
                .credentialStatus("null".equals(credentialStatus) ? null : credentialStatus)
                .build();

        var match = rule.evaluate(tx, bin);

        assertThat(match.score()).isEqualTo(expectedScore);
        assertThat(match.rule()).isEqualTo("CredentialStatusRule");
        assertThat(match.reason()).isEqualTo(expectedReason);
    }
}