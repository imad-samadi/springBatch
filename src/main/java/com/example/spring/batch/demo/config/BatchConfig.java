package com.example.spring.batch.demo.config;

import com.example.spring.batch.demo.entity.Employe;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.Repository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
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
import com.example.spring.batch.demo.repo.EmployeRepo ;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final EmployeRepo employeRepo;

    private final JobRepository jobRepository;
private final PlatformTransactionManager transactionManager;

    @Bean
    public FlatFileItemReader<Employe> itemReader() {

        FlatFileItemReader<Employe> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("src/main/resources/employe.csv"));
        reader.setName("EmployeeCSVReader");
        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper());
        return reader;

    }

    @Bean
    public EmployeProcessor empProcessor() {
        return new EmployeProcessor();
    }
    @Bean
    public RepositoryItemWriter<Employe> empWriter() {

        RepositoryItemWriter<Employe> writer = new RepositoryItemWriter<>();
        writer.setRepository(employeRepo);
        writer.setMethodName("save");
        return writer;

    }
    @Bean
    public Step step1() {
        return new StepBuilder("EmployeCSVImport",jobRepository)
                .<Employe,Employe>chunk(10,transactionManager)
                .reader(itemReader())
                .writer(empWriter())
                .processor(empProcessor())
                .build();
    }
    @Bean
    public Job  runJob() throws Exception {
        return new JobBuilder("ImportEmployeJob",jobRepository)
                .start(step1())
                .build();
    }



    private LineMapper<Employe> lineMapper() {

        DefaultLineMapper<Employe> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "name", "email", "phone");// to mape it to the employe object

        BeanWrapperFieldSetMapper<Employe> fieldSetMapper = new BeanWrapperFieldSetMapper<>(); //automatically maps CSV fields (after tokenization) to Java object fields.
        fieldSetMapper.setTargetType(Employe.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
}
