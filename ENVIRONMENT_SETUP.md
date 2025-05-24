# Environment Variables Setup

## Required Environment Variables

This application requires the following environment variables to be set:

### Database Configuration
- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username  
- `SPRING_DATASOURCE_PASSWORD`: Database password

### Admin Account Configuration
- `ADMIN_USERNAME`: Admin username (default: "admin")
- `ADMIN_PASSWORD`: Admin password (default: "admin123")

## Setup Instructions

### Option 1: Using .env file (Recommended and Automatic!)

**✨ The application now automatically loads `.env` files!**

1. Create a `.env` file in the project root directory:
```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/online_shop
SPRING_DATASOURCE_USERNAME=shop_user
SPRING_DATASOURCE_PASSWORD=shop_password

# Admin Account Configuration
ADMIN_USERNAME=admin
ADMIN_PASSWORD=your_secure_password_here
```

2. **Important**: Never commit the `.env` file to version control. It's already in `.gitignore`.

3. The application will automatically detect and load the `.env` file when starting up.

### Option 2: System Environment Variables

Set the environment variables in your system:

**Windows (PowerShell):**
```powershell
$env:ADMIN_USERNAME="admin"
$env:ADMIN_PASSWORD="your_secure_password"
```

**Windows (Command Prompt):**
```cmd
set ADMIN_USERNAME=admin
set ADMIN_PASSWORD=your_secure_password
```

**macOS/Linux:**
```bash
export ADMIN_USERNAME="admin"
export ADMIN_PASSWORD="your_secure_password"
```

### Option 3: IDE Configuration

Most IDEs allow you to set environment variables in run configurations:
- **IntelliJ IDEA**: Run/Debug Configurations → Environment Variables
- **Eclipse**: Run Configurations → Environment tab
- **VS Code**: Launch configuration in `.vscode/launch.json`

## Docker Compose

When using Docker Compose, environment variables are automatically loaded from a `.env` file in the project root.

## How It Works

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