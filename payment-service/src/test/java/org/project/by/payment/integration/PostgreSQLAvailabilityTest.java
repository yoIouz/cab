package org.project.by.payment.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.by.payment.testcontainers.PostgresTestContainer;
import org.project.by.payment.SucceededWatcher;
import org.project.by.payment.config.NoKafkaConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(NoKafkaConfiguration.class)
@ExtendWith(SucceededWatcher.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostgreSQLAvailabilityTest implements PostgresTestContainer {

    static JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setup() {
        jdbcTemplate = new JdbcTemplate(new DriverManagerDataSource(PostgresTestContainer.getUrl(),
                PostgresTestContainer.getUsername(),
                PostgresTestContainer.getPassword()));
    }

    @Test
    void testDatabaseConnection() {
        Boolean result = jdbcTemplate.queryForObject("SELECT to_regclass('public.transactions') IS NOT NULL",
                Boolean.class);
        assertThat(result).isTrue();
    }

}
