## Repository information

- Name: campaign-service
- Description: 

## Pre-requisites

- Java 17
- Gradle 8.0
- MySql
- Docker

## Setup Steps

- Any repository created will be dependent on the two libraries 
  - bannrx-common
  - utility
- `gradle.properties` file should be added in the repository root folder with the below environment variable
  - `environment` with the value as `local`
- A comment has been added to uncomment the below line in the file `setting.gradle.kts`. This should be followed.
- `.env.campaign-service` this file has environment variables required for local setup. Can ask for the same.
- `Loggable` annotation should be used at all the services and controller created. 
- Create database `bannrx` with password in local `Mysql` server. The `Mysql` can be anything i.e. by docker or local as per preference.
- For local set up of `Minio` as development environment AWS S3 equivalent, use the `docker-compose.yml` in [`docker`](./docker/) folder