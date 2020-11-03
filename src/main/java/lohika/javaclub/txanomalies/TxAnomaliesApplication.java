package lohika.javaclub.txanomalies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.PlatformTransactionManagerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootApplication
public class TxAnomaliesApplication {

    public static void main(String[] args) {
		startMariaDb();
		SpringApplication.run(TxAnomaliesApplication.class, args);
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
