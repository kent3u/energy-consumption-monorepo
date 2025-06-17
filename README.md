# Startup process
### Requirements
* Docker 27.5.1+

### Misc
Energy consumption app built with Spring Boot 3.5.0, PostgreSQL database and React FE.
Hexagonal architecture was used in the BE with sample tests provided for every layer. For database migration Liquibase was used. Integration tests use testcontainers.
Deployment is done via Docker Compose. Back-end uses default spring starter security (JSESSIONID) with bcrypt password hashing and in-memory session management. 

### Running application
* In the application root run `docker-compose up`
* Application starts latest postgreSQL container accessible on port `5432` next to 2 separate services - one for backend (port `8080`) and one for frontend (port `5173`).
  Currently database acts essentially as an in memory db since volume is not set. See `docker-compose.yml`.
* Front-end is accessible via `http://localhost:5173`.
* To connect to the database use following credentials:
   ```
  database: energy
  user: postgres
  password: postgres
   ```
* Liquibase is used to init database with 2 sample accounts:
  ```
  username: foobar
  password: password
  ____________________
  username: janedoe
  password: password
  ```

### Running tests
* Sample tests were created for each BE layer: controller, business, persistence (See `MeteringPointControllerTest.java`, `GetCustomerConsumptionDetailsTest.java`, `MeteringPointRepositoryAdapterTest.java`)
* In the application root navigate to `./backend` and run `./gradlew test`. (NB! requires JAVA_21 on host)
* Some tests were also created for front-end - requires npm and node (`10.9.2`, `v22.14.0` respectively were used during dev). Navigate to `./frontend` and run `npm i` followed by `npm test`.

### Possible improvements
* Negative energy prices were not taken into account - not sure about the business logic there.
* Resolution can be added along with some strategy pattern in `GetCustomerConsumptionDetails.java` to make component function for other query resolutions e.g. day, month.
* FE doesn't retain login status after refresh - could be solved with a 'test' endpoint like getCustomerDetails.
* FE is currently not responsive.
