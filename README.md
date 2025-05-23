# Basic Authentication and Authorization

A Spring Boot application implementing basic authentication and authorization using Spring Security.

## Project Overview

This is a Spring Boot application that demonstrates basic authentication and authorization using:
- Spring Security 6.1+
- Spring Boot 3.4.5
- Java 17
- PostgreSQL Database
- BCrypt Password Encoding

## Features

- Basic HTTP Authentication
- Role-based Authorization
- Secure endpoints configuration
- User management endpoints
- Password encryption using BCrypt

## Project Structure

```
src/main/java/com/example/basicAuthenticationAndAuthorization/
├── config/                  # Security configuration
│   ├── SecurityConfig.java  # Main security configuration
│   ├── UserInfoDetail.java  # User details implementation
│   └── UserInfoDetailService.java # User details service
├── controller/              # REST controllers
└── model/                   # Data models
```

## Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- PostgreSQL database

## Setup Instructions

1. Clone the repository
2. Configure your PostgreSQL database
3. Update the application.properties file with your database credentials
4. Build the project using Maven:
   ```bash
   ./mvnw clean install
   ```
5. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## Security Configuration

- CSRF protection is disabled
- Basic HTTP authentication is enabled
- Endpoints security:
  - `/userDetails/addNewUser` and `/userDetails/addNewSuperAdmin` - Public access
  - All other `/userDetails/**` endpoints - Authenticated access
  - All other endpoints - Authenticated access

## API Endpoints

- `POST /userDetails/addNewUser` - Add new user
- `POST /userDetails/addNewSuperAdmin` - Add new super admin
- Other endpoints require authentication

## Security Notes

- Passwords are encrypted using BCrypt
- Role-based access control is implemented
- CSRF protection is disabled for demonstration purposes
- HTTPS should be enabled in production

## License

This project is for educational purposes only.

## Contributing

Feel free to submit issues and enhancement requests!

## Support

If you have questions or need help, please open an issue.
