#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-INIT_SCRIPT
  CREATE USER dbusr WITH PASSWORD 'W4lFRuS0wosPePhL6Otr';
  CREATE DATABASE driver_schedule_db;

  GRANT ALL PRIVILEGES ON DATABASE driver_schedule_db TO dbusr;
  \c driver_schedule_db;

  CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
INIT_SCRIPT