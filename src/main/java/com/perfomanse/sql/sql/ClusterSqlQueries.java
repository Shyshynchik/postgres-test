package com.perfomanse.sql.sql;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClusterSqlQueries {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String DEFAULT_SELECT_ID_QUERY = "SELECT id FROM %s limit %s";
    private static final String DEFAULT_SELECT_QUERY = "SELECT * FROM %s WHERE id = '%s'";
    private static final String DEFAULT_SELECT_IN_QUERY = "SELECT * FROM %s WHERE id IN (:ids)";
    private static final String CLUSTER_TABLE = "cluster_table";
    private static final String NOT_CLUSTER_TABLE = "not_cluster_table";
    private static final int DEFAULT_EXECUTION_TIMES = 100;

    public void selectClusterOne() {
        var id = selectOneId();

        IntStream.range(0, DEFAULT_EXECUTION_TIMES)
                .mapToObj(i -> DEFAULT_SELECT_QUERY.formatted(CLUSTER_TABLE, id))
                .forEach(jdbcTemplate::execute);
    }

    public void selectNotClusterOne() {
        var id = selectOneId();

        IntStream.range(0, DEFAULT_EXECUTION_TIMES)
                .mapToObj(i -> DEFAULT_SELECT_QUERY.formatted(NOT_CLUSTER_TABLE, id))
                .forEach(jdbcTemplate::execute);
    }

    private UUID selectOneId() {
        return jdbcTemplate.queryForObject(DEFAULT_SELECT_ID_QUERY.formatted(CLUSTER_TABLE, 1), UUID.class);
    }

    public void selectClusterOneHundred() {
        var ids = selectOneHundredIds();
        var parameters = new MapSqlParameterSource("ids", ids);

        IntStream.range(0, DEFAULT_EXECUTION_TIMES)
                .mapToObj(i -> DEFAULT_SELECT_IN_QUERY.formatted(CLUSTER_TABLE))
                .forEach(query -> namedParameterJdbcTemplate.query(query, parameters, this::emptyMapper));
    }

    public void selectNotClusterOneHundred() {
        var ids = selectOneHundredIds();
        var parameters = new MapSqlParameterSource("ids", ids);

        IntStream.range(0, DEFAULT_EXECUTION_TIMES)
                .mapToObj(i -> DEFAULT_SELECT_IN_QUERY.formatted(NOT_CLUSTER_TABLE))
                .forEach(query -> namedParameterJdbcTemplate.query(query, parameters, this::emptyMapper));
    }

    private Object emptyMapper(ResultSet rs, int rowNum) {
        return new Object();
    }

    private List<UUID> selectOneHundredIds() {
        return jdbcTemplate.queryForList(DEFAULT_SELECT_ID_QUERY.formatted(CLUSTER_TABLE, 100), UUID.class);
    }

    public void selectClusterOneThousand() {
        var ids = selectOneThousandIds();
        var parameters = new MapSqlParameterSource("ids", ids);

        IntStream.range(0, DEFAULT_EXECUTION_TIMES)
                .mapToObj(i -> DEFAULT_SELECT_IN_QUERY.formatted(CLUSTER_TABLE))
                .forEach(query -> namedParameterJdbcTemplate.query(query, parameters, this::emptyMapper));
    }

    public void selectNotClusterOneThousand() {
        var ids = selectOneThousandIds();
        var parameters = new MapSqlParameterSource("ids", ids);

        IntStream.range(0, DEFAULT_EXECUTION_TIMES)
                .mapToObj(i -> DEFAULT_SELECT_IN_QUERY.formatted(NOT_CLUSTER_TABLE))
                .forEach(query -> namedParameterJdbcTemplate.query(query, parameters, this::emptyMapper));
    }

    private List<UUID> selectOneThousandIds() {
        return jdbcTemplate.queryForList(DEFAULT_SELECT_ID_QUERY.formatted(CLUSTER_TABLE, 1000), UUID.class);
    }

    public void selectClusterExceptOne() {
        var ids = selectTenThousandIds();
        var parameters = new MapSqlParameterSource("ids", ids);

        IntStream.range(0, DEFAULT_EXECUTION_TIMES)
                .mapToObj(i -> DEFAULT_SELECT_IN_QUERY.formatted(CLUSTER_TABLE))
                .forEach(query -> namedParameterJdbcTemplate.query(query, parameters, this::emptyMapper));
    }

    public void selectNotClusterExceptOne() {
        var ids = selectTenThousandIds();
        var parameters = new MapSqlParameterSource("ids", ids);

        IntStream.range(0, DEFAULT_EXECUTION_TIMES)
                .mapToObj(i -> DEFAULT_SELECT_IN_QUERY.formatted(NOT_CLUSTER_TABLE))
                .forEach(query -> namedParameterJdbcTemplate.query(query, parameters, this::emptyMapper));
    }

    private List<UUID> selectTenThousandIds() {
        return jdbcTemplate.queryForList(DEFAULT_SELECT_ID_QUERY.formatted(CLUSTER_TABLE, 10000), UUID.class);
    }
}
