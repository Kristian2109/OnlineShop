# Online Shop Backend

This is the backend service for the Online Shop application built with Spring Boot.

## Prerequisites

- Java 21
- Maven 3.9+
- MySQL 8.0
- Docker and Docker Compose (for production)

## Environment Variables Setup

### Required Environment Variables

This application requires the following environment variables to be set:

#### Database Configuration
- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username  
- `SPRING_DATASOURCE_PASSWORD`: Database password

#### Admin Account Configuration
- `ADMIN_USERNAME`: Admin username (default: "admin")
- `ADMIN_PASSWORD`: Admin password (default: "admin123")

### Setup Instructions

#### Option 1: Using .env file (Recommended and Automatic!)

**✨ The application now automatically loads `.env` files!**

1. Create a `.env` file in the project root directory:
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/online_shop
SPRING_DATASOURCE_USERNAME=shop_user
SPRING_DATASOURCE_PASSWORD=shop_password

ADMIN_USERNAME=admin
ADMIN_PASSWORD=your_secure_password_here
```

2. The application will automatically detect and load the `.env` file when starting up.

#### Option 2: System Environment Variables

Set the environment variables in your system:

**Windows (PowerShell):**
```powershell
$env:ADMIN_USERNAME="admin"
$env:ADMIN_PASSWORD="your_secure_password"
$env:SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/online_shop"
$env:SPRING_DATASOURCE_USERNAME="shop_user"
$env:SPRING_DATASOURCE_PASSWORD="shop_password"
```

**Windows (Command Prompt):**
```cmd
set ADMIN_USERNAME=admin
set ADMIN_PASSWORD=your_secure_password
set SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/online_shop
set SPRING_DATASOURCE_USERNAME=shop_user
set SPRING_DATASOURCE_PASSWORD=shop_password
```

**macOS/Linux:**
```bash
export ADMIN_USERNAME="admin"
export ADMIN_PASSWORD="your_secure_password"
export SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/online_shop"
export SPRING_DATASOURCE_USERNAME="shop_user"
export SPRING_DATASOURCE_PASSWORD="shop_password"
```

#### Option 3: IDE Configuration

Most IDEs allow you to set environment variables in run configurations:
- **IntelliJ IDEA**: Run/Debug Configurations → Environment Variables
- **Eclipse**: Run Configurations → Environment tab
- **VS Code**: Launch configuration in `.vscode/launch.json`

## Development Setup

### Running Locally

1. Clone the repository
2. Navigate to the project directory:
   ```bash
   cd simple
   ```
3. Create your `.env` file with the required environment variables (see above)
4. Run the application using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
5. The application will start on port 8001 (http://localhost:8001)

## Production Setup

### Using Docker Compose (Recommended)

1. Create a `.env` file in the project root with your production values
2. Run:
   ```bash
   docker-compose up -d
   ```

This will start both the MySQL database and the backend service.

### Using Docker

1. Ensure Docker is installed
2. Build and start the container:
   ```bash
   docker build -t online-shop-backend .
   docker run -p 8001:8001 \
     -e SPRING_DATASOURCE_URL=jdbc:mysql://your-mysql-host:3306/online_shop \
     -e SPRING_DATASOURCE_USERNAME=shop_user \
     -e SPRING_DATASOURCE_PASSWORD=your_password \
     -e ADMIN_USERNAME=admin \
     -e ADMIN_PASSWORD=your_admin_password \
     online-shop-backend
   ```

## How Environment Variables Work

The application includes a `DotEnvConfig` class that automatically:
- Loads the `.env` file from the project root
- Sets all variables as system properties
- Ignores missing files gracefully
- Provides console feedback on loading status

## Security Notes

- Use strong passwords for production environments
- Never hardcode credentials in source code
- Consider using secrets management systems for production deployments
- Regularly rotate admin passwords
- The `.env` file is already in `.gitignore` to prevent accidental commits
