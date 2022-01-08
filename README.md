# gafalag-api

## How to run
Gafalag API depends on database connection so make sure PostgreSQL database with correct scheme is running on port `5432`

Easiest way to start with development is to run database with Docker. Follow the steps below to do so:

### Run only database with Docker

To build PostgreSQL DB image enter command below in terminal from this project folder
```shell
docker build -t gafalag-api-db -f ./docker/db/Dockerfile .
```
After that enter command below to run Docker container of gafalag-api database
```shell
docker run -d -p 5432:5432 --name gafalag-api-db gafalag-api-db:latest
```
Once steps above are done simply run this java project from your IDE

### Run full project with Docker

It is also possible to run both API and DB from Docker at once. To do so execute following command in terminal:

```shell
docker-compose up -d
```

Only one of the 2 options should be chosen, running both will consume more memory and disc space 

## Empty all tables
```postgresql
DO $$ DECLARE
  r RECORD;
BEGIN
  FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP
    EXECUTE 'TRUNCATE TABLE ' || quote_ident(r.tablename) || ' CASCADE';
  END LOOP;
END $$;
```