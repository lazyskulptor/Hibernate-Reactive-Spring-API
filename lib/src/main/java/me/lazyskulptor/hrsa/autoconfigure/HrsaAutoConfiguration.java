package me.lazyskulptor.hrsa.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import me.lazyskulptor.hrsa.support.DefaultSessionDispatcher;
import me.lazyskulptor.hrsa.support.HrsaTransactionManager;
import me.lazyskulptor.hrsa.support.SessionDispatcher;
import me.lazyskulptor.hrsa.support.TransactionDispatcher;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.TransactionManager;

@Configuration
@Slf4j
@ConditionalOnClass(Mutiny.SessionFactory.class)
public class HrsaAutoConfiguration {


    @Bean
    public ReactiveTransactionManager transactionManager(@NonNull Mutiny.SessionFactory sessionFactory) {
        log.debug("init transactionManager with sessionFactory({})", sessionFactory);
        return new HrsaTransactionManager(sessionFactory);
    }

    @Bean
    @Primary
    public SessionDispatcher sessionDispatcher(@NonNull Mutiny.SessionFactory sessionFactory, TransactionManager transactionManager) {
        if (!(transactionManager instanceof HrsaTransactionManager)) {
            log.debug("init sessionDispatcher with {}", transactionManager);
            return new DefaultSessionDispatcher(sessionFactory);
        }
        log.debug("init sessionDispatcher as HrsaTransactionManager with {}", transactionManager);
        return new TransactionDispatcher(sessionFactory);
    }
}
