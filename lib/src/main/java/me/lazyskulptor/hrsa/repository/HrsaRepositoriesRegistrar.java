package me.lazyskulptor.hrsa.repository;

import me.lazyskulptor.hrsa.annotation.EnableHrsaRepositories;
import me.lazyskulptor.hrsa.support.ConfigurationExtensionSupport;
import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

public class HrsaRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {
    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableHrsaRepositories.class;
    }

    @Override
    protected RepositoryConfigurationExtension getExtension() {
        return new ConfigurationExtensionSupport();
    }
}
