package me.lazyskulptor.demo.model;

import me.lazyskulptor.demo.ContainerExtension;
import me.lazyskulptor.demo.repo.AccountQueryRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@SpringBootTest
@ExtendWith(ContainerExtension.class)
public class AccountTest {

    @Autowired
    AccountQueryRepository queryRepository;

    @Autowired
    Mutiny.SessionFactory sessionFactory;

    @Test
    void testSave() throws InterruptedException {

        Account account = Account.builder()
                .username(RandomStringUtils.randomAlphanumeric(16))
                .email(RandomStringUtils.randomAlphanumeric(8) + "@" + RandomStringUtils.randomAlphabetic(8) + ".com")
                .password(RandomStringUtils.randomAlphanumeric(16))
                .authorities(Set.of(Authority.MEMBER))
                .enabled(true)
                .build();

        var persisted = queryRepository.save(account)
//                .then(queryRepository.flush())
        .block();

        Thread.sleep(5000);

        System.out.println(persisted);
    }
}
