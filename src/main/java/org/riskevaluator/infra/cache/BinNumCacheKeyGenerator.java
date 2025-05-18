package org.riskevaluator.infra.cache;

import io.quarkus.cache.CacheKeyGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import org.riskevaluator.domain.model.Transaction;
import java.lang.reflect.Method;

@ApplicationScoped
public class BinNumCacheKeyGenerator implements CacheKeyGenerator {

    @Override
    public Object generate(Method method, Object... methodParams) {
        var tx = (Transaction) methodParams[0];
        return tx.hashCode();
    }
}