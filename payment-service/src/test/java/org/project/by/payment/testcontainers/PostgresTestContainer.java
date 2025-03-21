package org.project.by.payment.testcontainers;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public interface PostgresTestContainer {

    DockerImageName CAB_DB = DockerImageName
            .parse("cab_db:latest")
            .asCompatibleSubstituteFor("postgres");

    @Container
    PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(CAB_DB)
            .withDatabaseName("account")
            .withUsername("postgres")
            .withPassword("postgres")
            .withDatabaseName("account")
            .withInitScript("init.sql");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    static String getUrl() {
        return postgres.getJdbcUrl();
    }

    static String getUsername() {
        return postgres.getUsername();
    }

    static String getPassword() {
        return postgres.getPassword();
    }

}
