package org.project.by.passenger.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.by.passenger.testcontainers.PostgresTestContainer;
import org.project.by.passenger.SucceededWatcher;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
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
    void shouldCreateTable() {
        Boolean result = jdbcTemplate.queryForObject("SELECT to_regclass('public.passenger') IS NOT NULL",
                Boolean.class);
        assertThat(result).isTrue();
    }

}
