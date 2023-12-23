package me.lazyskulptor.demo.repo;

import me.lazyskulptor.demo.ContainerExtension;
import me.lazyskulptor.demo.model.Account;
import me.lazyskulptor.demo.model.Authority;
import me.lazyskulptor.demo.service.AccountService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.ReactiveTransactionManager;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(ContainerExtension.class)
public class TxManagerTest {

    @SpyBean
    ReactiveTransactionManager transactionManager;

    @SpyBean
    AccountQueryRepository queryRepository;
    @Autowired
    AccountService service;

    @Test
    void verifyCommitIsCalled() {
        final var value = 1L;
        doReturn(Mono.just(value)).when(queryRepository).count(any());
        var result = service.count().block();

        verify(transactionManager, times(1)).commit(any());
        verify(transactionManager, times(0)).rollback(any());

        assertThat(result).isEqualTo(value);
    }

    @Test
    void verifySaveIsCommitted() {
        Account account = Account.builder()
                .username(RandomStringUtils.randomAlphanumeric(16))
                .email(RandomStringUtils.randomAlphanumeric(8) + "@" + RandomStringUtils.randomAlphabetic(8) + ".com")
                .password(RandomStringUtils.randomAlphanumeric(16))
                .authorities(Set.of(Authority.MEMBER))
                .enabled(true)
                .build();

        service.save(account).block();
        var persisted = service.findAll().block();

        assertThat(persisted).isNotEmpty();
        verify(transactionManager, times(2)).commit(any());
        verify(transactionManager, times(0)).rollback(any());
    }

    @Test
    void verifyRollbackIsCalled() {
        try {
            doThrow(new RuntimeException("Test Exception")).when(queryRepository).count(any());

            service.count().block();
        } catch (Exception e) {}

        verify(transactionManager, times(0)).commit(any());
        verify(transactionManager, times(1)).rollback(any());
    }
}
