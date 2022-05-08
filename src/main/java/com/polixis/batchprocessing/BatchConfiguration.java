package com.polixis.batchprocessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.File;

// tag::setup[]
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	// end::setup[]

	@Value("file:src/main/resources/*.csv")
	private Resource[] inputResources;

	// tag::readerwriterprocessor[]
	@Bean
	public FlatFileItemReader<PersonDataSet> reader() {

		FlatFileItemReader<PersonDataSet> personDataSet = new FlatFileItemReader<PersonDataSet>();
		for (int i = 0; i < new File("src\\main\\resources").listFiles().length - 2; i++) {

			personDataSet = new FlatFileItemReaderBuilder<PersonDataSet>()
					.name("personItemReader")
					.resource(new FileSystemResource("src\\main\\resources\\" + i + ".csv"))
					.delimited()
					.names(new String[]{"date", "firstName", "lastName"})
					.fieldSetMapper(new BeanWrapperFieldSetMapper<PersonDataSet>() {{
						setTargetType(PersonDataSet.class);
					}})
					.build();
		}
		return personDataSet;
	}

	@Bean
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<PersonDataSet> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<PersonDataSet>()
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.sql("INSERT INTO people (first_name, last_name, date) VALUES (:firstName, :lastName, :date)")
			.dataSource(dataSource)
			.build();
	}
	// end::readerwriterprocessor[]

	// tag::jobstep[]
	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
		return jobBuilderFactory.get("importUserJob")
			.incrementer(new RunIdIncrementer())
			.listener(listener)
			.start(step1)
			.next(step2())
			.build();
	}

	@Bean
	public Step step1(JdbcBatchItemWriter<PersonDataSet> writer) {
		return stepBuilderFactory.get("step1")
			.<PersonDataSet, PersonDataSet> chunk(10)
			.reader(reader())
			.processor(processor())
			.writer(writer)
			.build();
	}

	@Bean
	public Step step2() {
		FileDeletingTasklet task = new FileDeletingTasklet();
		task.setResources(inputResources);
		return stepBuilderFactory.get("step2")
				.tasklet(task)
				.build();
	}
	// end::jobstep[]
}
