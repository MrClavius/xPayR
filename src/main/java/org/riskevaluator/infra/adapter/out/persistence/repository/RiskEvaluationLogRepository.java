package org.riskevaluator.infra.adapter.out.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.riskevaluator.infra.adapter.out.persistence.entity.RiskEvaluationLogEntry;

@ApplicationScoped
public class RiskEvaluationLogRepository implements PanacheRepository<RiskEvaluationLogEntry> {}