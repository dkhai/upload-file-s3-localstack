CREATE TABLE IF NOT EXISTS file_info (
    id SERIAL PRIMARY KEY,
    file_name varchar(255) NOT NULL,
    file_url varchar(328) NOT NULL,
    file_size numeric,
    user_id numeric,
    is_upload_success_full boolean
);

CREATE TABLE IF NOT EXISTS user_info (
    id SERIAL PRIMARY KEY,
    username varchar(20) NOT NULL,
    email varchar(20) NOT NULL,
    password varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS quota_configuration (
    userId numeric PRIMARY KEY,
    max_size numeric,
    used numeric,
    time_upload_per_day numeric
);