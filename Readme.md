# Hunting Gear Backend

This is the backend service for the Hunting Gear application built with Spring Boot.

## Prerequisites

- Java 21
- Maven 3.9+
- MySQL 8.0
- Docker and Docker Compose (for production)

## Development Setup

### Running Locally on Windows

1. Clone the repository
2. Navigate to the project directory:
   ```
   cd simple
   ```
3. Run the application using Maven:
   ```
   ./mvnw spring-boot:run
   ```
4. The application will start on port 8001 (http://localhost:8001)

### Environment Variables for Development

You can override application properties using environment variables:

```
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/hunting_gear_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=yourpassword
```

## Production Setup

### Using Docker

1. Ensure Docker and Docker Compose are installed
2. Create a `.env` file in the project root with:
   ```
   MYSQL_ROOT_PASSWORD=your_secure_password
   MYSQL_USER=hunting_gear_user
   MYSQL_PASSWORD=your_user_password
   ```
3. Build and start the container:
   ```
   docker build -t hunting-gear-backend .
   docker run -p 8001:8001 -e SPRING_DATASOURCE_URL=jdbc:mysql://your-mysql-host:3306/hunting_gear_db -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=your_password hunting-gear-backend
   ```

### Using Docker Compose (Recommended)

1. Create a `docker-compose.yml` file or use the one in the project
2. Configure environment variables in `.env` file
3. Run:
   ```
   docker-compose up -d
   ```

This will start both the MySQL database and the backend service.
