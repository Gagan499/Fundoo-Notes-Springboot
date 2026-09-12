# FundooNotes Backend

Backend REST API for the FundooNotes application, migrated to Java Spring Boot.

## Features

- User registration and authentication
- Create, view, update, and delete notes
- Note pinning, archiving, and trash management
- Label management
- Secure, token-based API access

## Technology Stack

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL

## Getting Started

### Prerequisites

- JDK 17 or later
- Maven
- MySQL

### Run locally

1. Create a MySQL database for the application.
2. Configure the database URL, username, and password in
	`src/main/resources/application.properties`.
3. Start the application:

	```bash
	./mvnw spring-boot:run
	```

The API is available at `http://localhost:8080`.

## API

Use the authentication endpoints to obtain a token, then include it in requests:

```http
Authorization: Bearer <token>
```

Common resources include `/users`, `/notes`, and `/labels`.
