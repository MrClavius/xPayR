package org.riskevaluator.app.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.riskevaluator.domain.policy.RiskRule;
import org.riskevaluator.domain.policy.rules.*;

import java.util.List;

@ApplicationScoped
public class RuleProvider {
    public List<RiskRule> provide() {
        return List.of(
            new AmountTransferRule(),
            new CountryMismatchRule(),
            new NonReloadableRule(),
            new AnonymousPrepaidIndicatorRule(),
            new ConsumerTypeRoleRule(),
            new CredentialStatusRule(),
            new FundingSourceRule(),
            new GamblingBlockRule(),
            new LocalUseRule()
        );
    }
}
