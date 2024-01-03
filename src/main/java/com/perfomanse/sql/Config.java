package com.perfomanse.sql;

import com.perfomanse.sql.sql.ClusterSqlQueries;
import com.perfomanse.sql.sql.SqlQueries;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@RequiredArgsConstructor
public class Config {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Bean
    public SqlQueries getSqlQueries() {
        var sqlQueries = new SqlQueries(jdbcTemplate);
//
//        sqlQueries.selectEnum();
//        sqlQueries.selectFkInt();
//        sqlQueries.selectFkUuid();
//        sqlQueries.selectFkVarchar();
//        sqlQueries.selectVarchar();
//
        return sqlQueries;
    }

    @Bean
    public ClusterSqlQueries getClusterSqlQueries() {
        var sqlQueries = new ClusterSqlQueries(jdbcTemplate, namedParameterJdbcTemplate);

        sqlQueries.selectClusterOne();
        sqlQueries.selectClusterExceptOne();
        sqlQueries.selectClusterOneHundred();
        sqlQueries.selectClusterOneThousand();

        sqlQueries.selectNotClusterOne();
        sqlQueries.selectNotClusterExceptOne();
        sqlQueries.selectNotClusterOneThousand();
        sqlQueries.selectNotClusterOneHundred();

        return sqlQueries;
    }
}
