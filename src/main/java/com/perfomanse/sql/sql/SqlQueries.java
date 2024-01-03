package com.perfomanse.sql.sql;


import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SqlQueries {
    private final JdbcTemplate jdbcTemplate;
    private static final String DEFAULT_QUERY = "SELECT * FROM %s WHERE status = %s";
    private static final int DEFAULT_EXECUTION_TIMES = 100;

    public void selectEnum() {
        IntStream.range(0, DEFAULT_EXECUTION_TIMES)
                .mapToObj(i -> DEFAULT_QUERY.formatted("users_enum", "'Active'::status_enum"))
                .forEach(jdbcTemplate::execute);
    }

    public void selectFkInt() {
        IntStream.range(0, DEFAULT_EXECUTION_TIMES)
                .mapToObj(i -> DEFAULT_QUERY.formatted("users_fk_int", "100200::int"))
                .forEach(jdbcTemplate::execute);
    }

    public void selectFkUuid() {
        IntStream.range(0, DEFAULT_EXECUTION_TIMES)
                .mapToObj(i -> DEFAULT_QUERY.formatted("users_fk_uuid", "'97453b14-e561-49a3-a99f-afefc6197156'::uuid"))
                .forEach(jdbcTemplate::execute);
    }

    public void selectFkVarchar() {
        IntStream.range(0, DEFAULT_EXECUTION_TIMES)
                .mapToObj(i -> DEFAULT_QUERY.formatted("users_fk_varchar", "'100200'::varchar"))
                .forEach(jdbcTemplate::execute);
    }

    public void selectVarchar() {
        IntStream.range(0, DEFAULT_EXECUTION_TIMES)
                .mapToObj(i -> DEFAULT_QUERY.formatted("users_varchar", "'Active'::varchar"))
                .forEach(jdbcTemplate::execute);
    }
}
