/*
 * dicom-walker_test: org.nrg.xnat.lambdas.dicom.walker.tests.OrmConfig
 * XNAT http://www.xnat.org
 * Copyright (c) 2005-2018, Washington University School of Medicine and Howard Hughes Medical Institute
 * All Rights Reserved
 *
 *  Released under the Simplified BSD.
 */

package org.nrg.xnat.lambdas.dicom.walker.tests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.Driver;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "org.nrg.xnat.lambdas.dicom.walker.repositories")
// @EnableCaching
@ComponentScan("org.nrg.xnat.lambdas.dicom.walker.services.impl.jpa")
@Slf4j
public class OrmConfig {
    @Bean
    public static FactoryBean<Properties> applicationProperties() {
        final YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        final Properties properties = yaml.getObject();
        if (properties == null) {
            throw new RuntimeException("Couldn't find application.yml properties");
        }
        return yaml;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySources() throws Exception {
        final Properties properties = applicationProperties().getObject();
        if (properties == null) {
            throw new RuntimeException("Couldn't find properties bean");
        }
        final PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setProperties(properties);
        return propertySourcesPlaceholderConfigurer;
    }

    @Bean
    public DataSource dataSource() throws Exception {
        final Properties properties = applicationProperties().getObject();
        if (properties == null) {
            throw new RuntimeException("Couldn't find properties bean");
        }

        final HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setJdbcUrl("jdbc:postgresql://localhost/dicom");
        dataSource.setUsername("dicom");
        dataSource.setPassword("");
        dataSource.setConnectionTestQuery("SELECT 1");
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(5);
        dataSource.setPoolName("dicom-walker");
        dataSource.setAutoCommit(false);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {
        final HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);

        final LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource());
        factory.setJpaVendorAdapter(adapter);
        factory.setJpaProperties(getJpaProperties());
        factory.setPackagesToScan("org.nrg.xnat.lambdas.dicom.walker.entities");
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .failOnEmptyBeans(false)
                .featuresToEnable(JsonParser.Feature.ALLOW_SINGLE_QUOTES, JsonParser.Feature.ALLOW_YAML_COMMENTS)
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS, SerializationFeature.WRITE_NULL_MAP_VALUES);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return objectMapperBuilder().build();
    }

    /*
    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManagerFactory().getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactory() {
        return new EhCacheManagerFactoryBean() {{
            setConfigLocation(new ClassPathResource("ehcache.xml"));
            setAcceptExisting(true);
            setShared(true);
        }};
    }
    */

    private Properties getJpaProperties() {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.dialect", "com.marvinformatics.hibernate.json.PostgreSQLJsonDialect");
        properties.setProperty("hibernate.use-new-id-generator-mappings", "true");
        return properties;
    }
}
