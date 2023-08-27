package me.lazyskulptor.hrsa.annotation;

import me.lazyskulptor.hrsa.autoconfigure.HrsaAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImportAutoConfiguration(value = HrsaAutoConfiguration.class)
public @interface EnableHibernateReactiveSpringAdapter {
}
