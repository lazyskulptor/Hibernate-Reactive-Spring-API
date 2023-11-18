package me.lazyskulptor.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.config.location=classpath:/application.yml")
@ExtendWith(ContainerExtension.class)
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
