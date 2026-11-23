# Employee Management

A sample Spring Boot REST API with CRUD operations for employees, using an H2 in-memory database and Docker for running.

- Java 21, Spring Boot 4.2.0, Spring Data JPA, H2, Bean Validation
- Base package: `com.dxc.rf.api`

## Run with Docker

```bash
docker build -t employee-management .
docker run --rm -p 8080:8080 employee-management
```

## Run locally (Maven Wrapper)

```bash
./mvnw test
./mvnw spring-boot:run
```

## API

Base URL: `http://localhost:8080/api/employees`

| Method | Path | Description | Success |
|--------|------|-------------|---------|
| POST | `/api/employees` | Create an employee | 201 |
| GET | `/api/employees` | List all employees | 200 |
| GET | `/api/employees/{id}` | Get one employee | 200 |
| PUT | `/api/employees/{id}` | Update an employee | 200 |
| DELETE | `/api/employees/{id}` | Delete an employee | 204 |

Employee JSON:

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": "Sales",
  "salary": 70000.00
}
```

### curl examples

```bash
# List
curl -s http://localhost:8080/api/employees

# Create
curl -s -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","email":"john.doe@example.com","department":"Sales","salary":70000}'

# Get by id
curl -s http://localhost:8080/api/employees/1

# Update
curl -s -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","email":"john.doe@example.com","department":"Marketing","salary":75000}'

# Delete
curl -i -X DELETE http://localhost:8080/api/employees/1
```

Validation errors return 400 and conflicts (duplicate email) return 409. Unknown ids return 404.

## Notes

- The H2 database is in memory: data resets each time the application starts. Three sample employees are loaded from `src/main/resources/data.sql`.
- H2 console: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:employeedb`, user `sa`, empty password).
