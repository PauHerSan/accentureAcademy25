package com.superStore.superStoreData.storeConfiguration;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class inMemoryBatchConfig {

    @Bean("h2DataSource")
    public DataSource h2DataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .build();
    }

    @Bean("batchTransactionManager")
    public PlatformTransactionManager batchTransactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    @Primary
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(h2DataSource());
        factory.setTransactionManager(batchTransactionManager());
        factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");
        factory.afterPropertiesSet();
        return factory.getObject();
    }



}

