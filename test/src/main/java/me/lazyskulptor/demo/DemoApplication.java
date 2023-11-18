package me.lazyskulptor.demo;

import me.lazyskulptor.hrsa.annotation.EnableHrsaRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableHrsaRepositories(basePackages = "me.lazyskulptor.demo.repo")
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
