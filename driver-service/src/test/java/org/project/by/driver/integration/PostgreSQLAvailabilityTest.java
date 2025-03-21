package org.project.by.driver.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project.by.driver.SucceededWatcher;
import org.project.by.driver.testcontainers.PostgresTestContainer;
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
        Boolean isDriverTableCreated =
                jdbcTemplate.queryForObject("SELECT to_regclass('public.driver') IS NOT NULL", Boolean.class);
        Boolean isDriverStatusTableCreated =
                jdbcTemplate.queryForObject("SELECT to_regclass('public.driver_status') IS NOT NULL", Boolean.class);

        assertThat(isDriverTableCreated).isTrue();
        assertThat(isDriverStatusTableCreated).isTrue();
    }

}
