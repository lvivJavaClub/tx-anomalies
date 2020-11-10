package lohika.javaclub.txanomalies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@SpringBootApplication
public class TxAnomaliesApplication {

    public static void main(String[] args) {
//		startPostgresql();
		startMariaDb();
		SpringApplication.run(TxAnomaliesApplication.class, args);
    }

    @Bean(name = "jdbcTxManager")
	public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
		var txManager = new DataSourceTransactionManager();
		txManager.setDataSource(dataSource);
		return txManager;
	}

	@Bean(name = {"jpaTxManager", "transactionManager"})
	public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
    	return new JpaTransactionManager(emf);
	}

	private static void startPostgresql() {
		var postgreSQLContainer = new PostgreSQLContainer("postgres:11")
				.withDatabaseName("javaclub")
				.withUsername("javadev")
				.withPassword("secret");

		postgreSQLContainer.start();
		System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
	}

	private static void startMariaDb() {
		var mariaDb = new MariaDBContainer<>("mariadb:10")
				.withDatabaseName("javaclub")
				.withUsername("javadev")
				.withPassword("secret");

		mariaDb.start();
		System.setProperty("spring.datasource.url", mariaDb.getJdbcUrl());
	}

}
