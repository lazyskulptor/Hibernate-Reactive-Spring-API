package me.lazyskulptor.hrsa.support;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.NonNull;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.reactive.TransactionSynchronizationManager;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
public class TransactionDispatcher implements InitializingBean, SessionDispatcher {

    @NonNull
    private final Mutiny.SessionFactory sessionFactory;

    public TransactionDispatcher(@NonNull Mutiny.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        afterPropertiesSet();
    }

    @Override
    public Mutiny.SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    @Override
    public <R> Mono<R> apply(Function<Mutiny.Session, Uni<R>> work) {
        return TransactionSynchronizationManager.forCurrentTransaction()
                .mapNotNull(syncManager -> (HrsaSessionHolder)syncManager.getResource(this.getSessionFactory()))
                .onErrorResume(NoTransactionException.class, (e) -> Mono.empty())
                .mapNotNull(HrsaSessionHolder::getSession)
                .map(work)
                .switchIfEmpty(Mono.just(this.getSessionFactory()
                        .withSession(session -> work.apply(session)
                                .call(session::flush)
                                .log("LOCAL SESSION[" + session.hashCode() + "]"))))
                .flatMap(uni -> uni.convert().with(UniReactorConverters.toMono()));
    }

    @Override
    public void afterPropertiesSet() {
        if (this.getSessionFactory() == null) {
            throw new IllegalArgumentException("Property 'connectionFactory' is required");
        }
    }
}
