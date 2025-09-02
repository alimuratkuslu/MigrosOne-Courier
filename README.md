# Courier Tracking System

Restful Spring Boot application that accepts streaming locations of couriers, tracks them and detects when courier enters 100m radius of a Migros Store

## Architecture

- **Hexagonal Architecture (Ports & Adapters)**
- **Design Patterns**:
  - **Strategy Pattern**: Total distance calculation algorithm (Haversine formula)
  - **Observer Pattern**: Store entry event notification/logging

### Running the Application

#### Option 1: With Maven
1. Clone and navigate to the project directory
2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The application will start on `http://localhost:8080`

#### Option 2: With Docker
1. Clone and navigate to the project directory
2. Build and run with Docker Compose:
   ```bash
   docker-compose up --build
   ```
3. The application will start on `http://localhost:8080`

To stop the application:
```bash
docker-compose down
```

### H2 In-Memory Database Access
- H2 Console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:courierdb`
- Username: `migros-one`
- Password: (empty)

## API Endpoints

### Submit Courier Location
```http
POST /api/courier-location
Content-Type: application/json

{
  "time": "2024-01-01T10:00:00",
  "courierId": "courier-1",
  "lat": 40.9923307,
  "lng": 29.1244229
}
```

### Get Total Travel Distance
```http
GET /api/courier/{courierId}/total-distance
```

## Testing the Application

### Postman Collection

1. **Start the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

2. Import the `MigrosOne.postman_collection.json` to Postman and test the application using those requests 
