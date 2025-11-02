# Weather Station API Test Suite

This project contains automated API tests for the OpenWeatherMap Weather Station API using Java, Maven, TestNG, and RestAssured.

## Features

- Register, retrieve, update, and delete weather stations via API
- Validates API responses and error handling
- Uses TestNG for test management and assertions

## Tech Stack

- Java
- Maven
- TestNG
- RestAssured
- org.json

## Project Structure

- `src/test/java/Tests/WeatherTest.java` - Main test class for Weather Station API
- `PayloadBuilder/WeatherStationPayload.java` - Utility for building request payloads
- `Utils/LoggerUtil.java` - Logging utility
- `Common/commonTestData.java` - Common test data and status codes

## Prerequisites

- Java 11 or higher
- Maven 3.x

## Setup

1. Clone the repository:
   ```
   git clone <your-repo-url>
   cd <project-directory>
   ```

2. Install dependencies:
   ```
   mvn clean install
   ```

## Running the Tests

To execute all tests:
```
mvn test
```

## Configuration

- The API key is hardcoded in the test class for demonstration. Replace with your own if needed.
- Base URI: `http://api.openweathermap.org/data/3.0`

## Notes

- Ensure your API key is valid and has permissions for the Weather Station API.
- Tests create and delete stations dynamically; no manual cleanup is required.

---

**Author:** Pkhuse  
**License:** MIT

