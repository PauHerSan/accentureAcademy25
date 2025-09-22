package com.superStore.superStoreData.storeConfiguration;

import com.superStore.superStoreData.storeModel.customer;
import com.superStore.superStoreData.storeModel.customerMongo;
import com.superStore.superStoreData.storeRepo.storeMongoRepository;
import com.superStore.superStoreData.storeRepo.storeRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class springBatchConfig {

    private JobRepository jobRepository;

    private PlatformTransactionManager platformTransactionManager;


    private storeRepository storeRepository;

    private storeMongoRepository storeMongoRepository;


    @Bean
    public FlatFileItemReader<customer> reader() {
        FlatFileItemReader<customer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/superstore_data.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<customer> lineMapper() {
        DefaultLineMapper<customer> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("Id", "Year_Birth", "Education", "Marital_Status", "Income", "MntFruits", "MntMeatProducts", "MntFishProducts","MntSweetProducts");

        BeanWrapperFieldSetMapper<customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(customer.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }

    @Bean
    public educationProcessor educationProcessor() {
        return new educationProcessor();
    }

    @Bean
    public maritalStatusProcessor maritalStatusProcessor() {
        return new maritalStatusProcessor();
    }

    @Bean
    public RepositoryItemWriter<customer> writer() {
        RepositoryItemWriter<customer> writer = new RepositoryItemWriter<>();
        writer.setRepository(storeRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public RepositoryItemReader<customer> databaseReader() {
        RepositoryItemReader<customer> reader = new RepositoryItemReader<>();
        reader.setRepository(storeRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(10);

        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        reader.setSort(sorts);

        return reader;
    }

    @Bean
    public RepositoryItemWriter<customerMongo> mongoWriter() {
        RepositoryItemWriter<customerMongo> writer = new RepositoryItemWriter<>();
        writer.setRepository(storeMongoRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step step1() {
        return new StepBuilder("csv-step", jobRepository)
                .<customer, customer>chunk(10, platformTransactionManager)
                .reader(reader())
                .processor(educationProcessor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step step2() {
        return new StepBuilder("second-step", jobRepository)
                .<customer, customerMongo>chunk(10, platformTransactionManager)
                .reader(databaseReader())
                .processor(maritalStatusProcessor())
                .writer(mongoWriter())
                .build();
    }

    @Bean
    public Job runJob() {
        return new JobBuilder("importCustomers", jobRepository)
                .flow(step1())
                .next(step2())
                .end().build();

    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }


}
