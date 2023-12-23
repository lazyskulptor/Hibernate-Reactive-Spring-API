package me.lazyskulptor.demo.model;

import me.lazyskulptor.demo.ContainerExtension;
import me.lazyskulptor.demo.IdEqualsSpec;
import me.lazyskulptor.demo.repo.AccountQueryRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(ContainerExtension.class)
public class AccountTest {

    @Autowired
    AccountQueryRepository queryRepository;

    @Test
    void testSave() {
        Account account = Account.builder()
                .username(RandomStringUtils.randomAlphanumeric(16))
                .email(RandomStringUtils.randomAlphanumeric(8) + "@" + RandomStringUtils.randomAlphabetic(8) + ".com")
                .password(RandomStringUtils.randomAlphanumeric(16))
                .authorities(Set.of(Authority.MEMBER))
                .enabled(true)
                .build();

        queryRepository.save(account).block();
        var persisted = queryRepository.findOne(new IdEqualsSpec(account.getId())).block();

        assertThat(persisted).isNotNull();
    }
}
