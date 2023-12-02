FROM postgres:16.1

CMD ["postgres", "-c", "shared_preload_libraries=pg_stat_statements", "-c", "pg_stat_statements.track=all", "-c", "max_connections=200"]

COPY init.sql /docker-entrypoint-initdb.d/init.sql