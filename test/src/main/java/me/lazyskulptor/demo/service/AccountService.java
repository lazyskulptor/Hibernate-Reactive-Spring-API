package me.lazyskulptor.demo.service;

import me.lazyskulptor.demo.model.Account;
import me.lazyskulptor.demo.repo.AccountQueryRepository;
import me.lazyskulptor.demo.spec.Logic;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Transactional
@Service
public class AccountService {
    private final AccountQueryRepository queryRepository;

    public AccountService(AccountQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public Mono<Long> count() {
        return queryRepository.count(Logic.TRUE);
    }

    public Mono<List<Account>> findAll() {
        return queryRepository.findAll(Sort.by("id"))
                .collectList();
    }

    public Mono<Void> save(Account account) {
        return queryRepository.saveAndFlush(account)
                .then();
    }
}
