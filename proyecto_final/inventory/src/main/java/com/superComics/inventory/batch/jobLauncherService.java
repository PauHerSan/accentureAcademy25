package com.superComics.inventory.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

@Service
public class jobLauncherService {

    private final JobLauncher jobLauncher;
    private final Job inventoryReportJob; // Inyectar el Job que definimos

    public jobLauncherService(JobLauncher jobLauncher, Job inventoryReportJob) {
        this.jobLauncher = jobLauncher;
        this.inventoryReportJob = inventoryReportJob;
    }

    /**
     * Lanza la ejecución del Job de Reporte de Inventario.
     *  jobName El nombre del Job a lanzar.
     */
    public String runInventoryReportJob(String jobName) {
        try {
            // Un JobParameters debe ser único para cada ejecución.
            // Usamos el timestamp para asegurar la unicidad.
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("jobName", jobName)
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(inventoryReportJob, jobParameters);
            return "Job '" + jobName + "' lanzado exitosamente.";

        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {

            return "Error al lanzar el Job '" + jobName + "': " + e.getMessage();
        }
    }

}
