package com.superComics.inventory.batch;

import com.superComics.inventory.inventory.model.comicItem;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class inventoryJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory; // Necesario para leer entidades JPA

    // Inyección de dependencias (Spring Boot proporciona estos beans)
    public inventoryJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, EntityManagerFactory entityManagerFactory) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * 1. Lector (Reader): Lee entidades comicItem de la base de datos MySQL.
     * Usamos JpaPagingItemReader para manejar grandes volúmenes de datos de forma eficiente.
     */
    @Bean
    public ItemReader<comicItem> comicItemReader() {
        return new JpaPagingItemReaderBuilder<comicItem>()
                .name("comicItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT c FROM comicItem c") // Lee todas las entidades
                .pageSize(100) // Lee en chunks de 100
                .build();
    }

    /**
     * 2. Procesador (Processor): Lógica de negocio.
     * En este ejemplo, verifica si un cómic tiene el campo publisher no válido.
     */
    @Bean
    public ItemProcessor<comicItem, String> comicItemProcessor() {
        return item -> {
            if (item.getPublisher() == null || item.getPublisher().trim().isEmpty()) {
                return "⚠️ [ID " + item.getId() + "] Título: '" + item.getTitle() + "' - SIN PUBLICHER DEFINIDO.";
            }
            if (item.needRestock()) { // Uso de la lógica del modelo
                return "🚨 [ID " + item.getId() + "] Título: '" + item.getTitle() + "' - ALERTA: STOCK BAJO (" + item.getCurrentStock() + ").";
            }
            return null; // Devuelve null para filtrar los ítems válidos (los que no son problema)
        };
    }

    /**
     * 3. Escritor (Writer): Escribe los resultados del procesamiento.
     * Usamos un escritor simple para imprimir a la consola/log.
     */
    @Bean
    public ItemWriter<String> reportWriter() {
        return items -> {
            System.out.println("--- Reporte de Cumplimiento de Inventario ---");
            items.forEach(System.out::println);
            System.out.println("--------------------------------------------");
        };
    }

    /**
     * 4. Paso (Step): Define la secuencia Reader -> Processor -> Writer.
     */
    @Bean
    public Step inventoryReportStep(ItemReader<comicItem> reader, ItemProcessor<comicItem, String> processor, ItemWriter<String> writer) {
        return new StepBuilder("inventoryReportStep", jobRepository)
                .<comicItem, String>chunk(10, transactionManager) // Procesa 10 registros a la vez
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    /**
     * 5. Tarea (Job): Agrupa los pasos.
     */
    @Bean
    public Job inventoryReportJob(Step inventoryReportStep) {
        return new JobBuilder("inventoryReportJob", jobRepository)
                .start(inventoryReportStep)
                .build();
    }
}
