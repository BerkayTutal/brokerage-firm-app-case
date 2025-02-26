# Brokerage Firm Application

This is a Spring Boot application designed to manage the operations of a brokerage firm. The application interacts with a MySQL database and exposes RESTful APIs for managing data.

---

## Features
- **REST API**: Interact with the system easily via REST endpoints.
- **Database Integration**: Uses MySQL for data persistence.
- **Dockerized**: Easily run and deploy the application with Docker.
- **Postman Collection**: Helps you test the available API endpoints.

---

## Prerequisites
Before running the application, ensure you have the following installed:
1. [Docker](https://www.docker.com/)
2. [Docker Compose](https://docs.docker.com/compose/install/)
3. Optional: If running locally without Docker, ensure you have:
   - [Java 23 JDK](https://www.oracle.com/za/java/technologies/javase-jdk23-downloads.html)
   - [Maven 3.9](https://maven.apache.org/download.cgi)
   - [A MySQL Server](https://dev.mysql.com/downloads/installer/)
4. [Postman](https://www.postman.com/downloads/) (optional, for testing APIs).

---

## Running the Application with Docker

Follow these steps to run the application easily using Docker and Docker Compose:

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd <repository-name>
   ```

2. **Build and Start Services**:
   Run the following command to build and start both the MySQL database and the application:
   ```bash
   docker-compose up --build
   ```
   **Note**: This will take a few minutes to complete.


3. **Running the Local Configuration**

   If you want to run the application on testing configuration, run the following command:
   ```bash
   SPRING_PROFILES_ACTIVE=local docker-compose up --build
   ```


4. **Access the Application**:
   - The backend Spring application runs at [http://localhost:8080](http://localhost:8080).
   - The MySQL database runs at `localhost:3306`.

5. **Database Configuration**:
   The application uses the following default credentials to connect to the MySQL database:
   - **URL**: `jdbc:mysql://mysqldb:3306/brokerage?useSSL=false&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true`
   - **Username**: `root`
   - **Password**: `root`

   These credentials and database settings are configured in the `docker-compose.yml` file through environment variables.

---

## Building and Running the Application Manually (Without Docker)

1. **Install dependencies**:
   Ensure you have Java, Maven, and a MySQL server installed.

2. **Set Up the Database**:
   - Create a database named `brokerage`.
   - Update the database connection details (if necessary) in `application.properties`:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/brokerage?useSSL=false&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true
     spring.datasource.username=root
     spring.datasource.password=root
     ```

3. **Build the Project**:
   Run the following Maven command to build the `.jar` file:
   ```bash
   mvn clean install
   ```

4. **Run the Application**:
   Start the application with:
   ```bash
   java -jar target/brokerage-firm-0.0.1-SNAPSHOT.jar
   ```

5. **Access the Application**:
   - The backend application will be running at [http://localhost:8080](http://localhost:8080).

---

## Testing the API with Postman

A Postman collection has been included in the project to help you test the available API endpoints.

1. Open Postman.
2. Import the file `Broker Firm Application.postman_collection.json` found in the project repository.
3. **Environment Variables** (if required):
   - Add a `BEARER_ADMIN` variable for the admin token.
   - Add a `BEARER_CUSTOMER_2` variable for the customer token.

   These variables are used to authenticate certain requests included in the collection.

4. Run the predefined requests in the collection:
   - **Register and Login**: Test the registration and login APIs.
   - **Customers**: Fetch or manipulate customer-related data (requires appropriate tokens).

---


## Development Notes

### Running Tests
To run the application's unit and integration tests, execute:
```bash
mvn test
```

### Maven Dependency Management
If you're using a local Maven repository, ensure it's accessible inside the container. In the `docker-compose.yml`, the volume `- .m2:/root/.m2` ensures reuse of dependencies.

---

## Troubleshooting
1. **MySQL Connection Errors**:
   - Ensure that the MySQL service (container) is running and available.
   - Verify the database credentials in `docker-compose.yml` or `application.properties`.

2. **Port Conflicts**:
   - If port `8080` or `3306` is already in use, update the ports in `docker-compose.yml` under `ports`:
     ```yaml
     ports:
       - <new-port>:8080
       - <new-port>:3306
     ```

3. **Dependency Issues**:
   - Run `mvn clean install` again to resolve any issues with the build.

---

## Future Improvements
- Integrating Swagger UI for documenting the API.
- Adding CI/CD pipelines (e.g., with GitHub Actions or Jenkins).

