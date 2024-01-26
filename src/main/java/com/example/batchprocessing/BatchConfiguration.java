package com.example.batchprocessing;

import javax.activation.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration // クラスに必ずつける
@EnableBatchProcessing // Spring Batch機能が有効になる(バッチ処理が実行されるようになる)
public class BatchConfiguration {

    // JobBuilderオブジェクトを生成するためのクラスです
    public JobBuilderFactory jobBuilderFactory;

    // StepBUilderオブジェクトを生成するクラスです
    public StepBuilderFactory stepBuilderFactory;

    // ビルダークラスをインジェクションする
    @Autowired
    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;

    }

    // ItemProcessorのインスタンスを返す
    @Bean // メソッドに必ずつける
    public EmployeeItemProcessor processor() {
        return new EmployeeItemProcessor();
    }
    
    // ItemReader用のメソッドです
    @Bean
    public FlatFileItemReader<Employee> reader() {
        return new FlatFileItemReaderBuilder<Employee>()
            .name("employeeItemReader") // (1) リーダーの名前を設定します
            .resource(new ClassPathResource("employee.csv")) // (2) 入力に利用されるデータやファイルを指定します
            .delimited() // (3) CSVファイルをカンマ区切りに分割します
            .names(new String[]{"name", "department"}) // (4) 区切った要素ごとにフィールド名を与えます
            .fieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {{
                setTargetType(Employee.class);
            }}) // (5) 取り出した情報をもとにEmployeeクラスのオブジェクトを生成します
            .build(); // (6) FlatFileItemReader型のオブジェクトを生成します
    }
    
     // ItemWriter用のメソッドです
    @Bean
    public JdbcBatchItemWriter<Employee> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Employee>() // (1) ItemWriterのインスタンスを生成します
            // (2)パラメータ付きのSQLを実行するためのオブジェクトをセットします
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            // (3) SQL文をセットします
            .sql("INSERT INTO EMPLOYEES (ID, NAME, DEPARTMENT) VALUES (EMPLOYEE_ID_SEQ.nextval, :name, :department)")
            // (4) パラメータをセットします
            .dataSource(dataSource)
            .build(); // (5) JdbcBatchItemWriter<Employee>のオブジェクトを生成します
    }
}