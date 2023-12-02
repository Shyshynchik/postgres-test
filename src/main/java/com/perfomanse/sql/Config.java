package com.perfomanse.sql;

import com.perfomanse.sql.sql.SqlQueries;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
public class Config {
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public SqlQueries getSqlQueries() {
        var sqlQueries = new SqlQueries(jdbcTemplate);

        sqlQueries.selectEnum();
        sqlQueries.selectFkInt();
        sqlQueries.selectFkUuid();
        sqlQueries.selectFkVarchar();
        sqlQueries.selectVarchar();

        return sqlQueries;
    }
}
