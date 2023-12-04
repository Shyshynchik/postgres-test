DROP SCHEMA IF EXISTS public;
CREATE SCHEMA public;

CREATE EXTENSION IF NOT EXISTS pg_stat_statements;

CREATE TABLE user_status_int
(
    code int primary key,
    name varchar
);

INSERT INTO user_status_int (code, name) VALUES (100200, 'Active');
INSERT INTO user_status_int (code, name) VALUES (100300, 'Inactive');
INSERT INTO user_status_int (code, name) VALUES (100400, 'Canceled');

CREATE TABLE user_status_varchar
(
    code varchar(8) primary key,
    name varchar
);

INSERT INTO user_status_varchar (code, name) VALUES ('100200', 'Active');
INSERT INTO user_status_varchar (code, name) VALUES ('100300', 'Inactive');
INSERT INTO user_status_varchar (code, name) VALUES ('100400', 'Canceled');

CREATE TABLE user_status_uuid
(
    code uuid primary key,
    name varchar
);

INSERT INTO user_status_uuid (code, name) VALUES ('97453b14-e561-49a3-a99f-afefc6197156', 'Active');
INSERT INTO user_status_uuid (code, name) VALUES ('068b39a2-c369-41f3-b8a1-26864de04fba', 'Inactive');
INSERT INTO user_status_uuid (code, name) VALUES ('01948add-f1dd-4a22-b754-723dee93d516', 'Canceled');

CREATE TYPE status_enum AS ENUM ('Active', 'Canceled', 'Inactive');

CREATE TABLE users_enum
(
    user_id uuid primary key,
    login   varchar not null,
    email   varchar not null,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    status status_enum not null
);

INSERT INTO users_enum (user_id, login, email, status)
select gen_random_uuid() as uuid,
       md5(random()::text),
       md5(random()::text),
       (array ['Active'::status_enum, 'Canceled'::status_enum, 'Inactive'::status_enum])[floor(random() * 3 + 1)]
from generate_series(1, 500000) s(i);

CREATE TABLE users_fk_int
(
    user_id uuid primary key,
    login   varchar not null,
    email   varchar not null,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    status int not null references user_status_int(code)
);

INSERT INTO users_fk_int (user_id, login, email, status)
select
    user_id,
    login,
    email,
    CASE
        WHEN status = 'Active' THEN 100200
        WHEN status = 'Inactive' THEN 100300
        WHEN status = 'Canceled' THEN 100400
    END
from users_enum;

CREATE TABLE users_fk_varchar
(
    user_id uuid primary key,
    login   varchar not null,
    email   varchar not null,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    status varchar(8) not null references user_status_varchar(code)
);

INSERT INTO users_fk_varchar (user_id, login, email, status)
select
    user_id,
    login,
    email,
    CASE
        WHEN status = 'Active' THEN '100200'
        WHEN status = 'Inactive' THEN '100300'
        WHEN status = 'Canceled' THEN '100400'
    END
from users_enum;

CREATE TABLE users_fk_uuid
(
    user_id uuid primary key,
    login   varchar not null,
    email   varchar not null,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    status uuid not null references user_status_uuid(code)
);

INSERT INTO users_fk_uuid (user_id, login, email, status)
select
    user_id,
    login,
    email,
    CASE
        WHEN status = 'Active' THEN '97453b14-e561-49a3-a99f-afefc6197156'::uuid
        WHEN status = 'Inactive' THEN '068b39a2-c369-41f3-b8a1-26864de04fba'::uuid
        WHEN status = 'Canceled' THEN '01948add-f1dd-4a22-b754-723dee93d516'::uuid
    END
from users_enum;

CREATE TABLE users_varchar
(
    user_id uuid primary key,
    login   varchar not null,
    email   varchar not null,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    status varchar(8) not null
);

INSERT INTO users_varchar (user_id, login, email, status)
select
    user_id,
    login,
    email,
    CASE
        WHEN status = 'Active' THEN 'Active'
        WHEN status = 'Inactive' THEN 'Inactive'
        WHEN status = 'Canceled' THEN 'Canceled'
        END
from users_enum;

SELECT
    CASE
        WHEN query like '%users_fk_int%' THEN 'fk_int'
        WHEN query like '%users_enum%' THEN 'enum'
        WHEN query like '%users_fk_varchar%' THEN 'fk_varchar'
        WHEN query like '%users_fk_uuid%' THEN 'fk_uuid'
        WHEN query like '%users_varchar%' THEN 'varchar'
        END as type,
    calls,
    total_exec_time,
    min_exec_time,
    max_exec_time,
    mean_exec_time,
    rows
from pg_stat_statements
where query like 'SELECT * FROM users_enum WHERE status =%'
   or query like 'SELECT * FROM users_fk_int WHERE status =%'
   or query like 'SELECT * FROM users_fk_uuid WHERE status =%'
   or query like 'SELECT * FROM users_fk_varchar WHERE status =%'
   or query like 'SELECT * FROM users_varchar WHERE status =%';