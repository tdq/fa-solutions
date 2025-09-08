# FA Solutions Report Application

This is a Back-end assignment Spring Boot application that provides an endpoint to generate reports of portfolio transactions.  
Authentication is enabled via Spring Security with a basic in-memory user.

## Authentication

To access the endpoints, you must authenticate with HTTP Basic Authentication.

**Default credentials:**

- **Username:** `user`
- **Password:** `password`

## REST Endpoint

### `GET /report`

Generates a transaction report for a given portfolio within a date range.

#### Request Parameters

|Name|Type|Required|Description|
|----|----|-----|-----------|
|`portfolioId`|`Long`|✅ Yes|ID of the portfolio to generate the report for.|
|`from`|`LocalDate` (yyyy-MM-dd)|❌ Optional|Start date (inclusive). If not provided, no lower bound is applied.|
|`to`|`LocalDate` (yyyy-MM-dd)|❌ Optional|End date (inclusive). If not provided, no upper bound is applied.|

#### Example Request

```bash
curl -u user:password "http://localhost:8080/report?portfolioId=3&from=2025-09-01&to=2025-09-08"
```

## Configuration

The application supports configuration via application.properties or environment variables.

| Property          | Env Variable  | Default   | Description                                           |
|-------------------|---------------|-----------|-------------------------------------------------------|
| tryme.username    | USERNAME      | (none)    | Username used for integration with external services  |
| tryme.password    | PASSWORD	    | (none)	| Password for the above username                       |
| tryme.auth.path   | AUTH_PATH	    | (none)	| Path/URL for authentication service                   |
| tryme.graphql.path| GRAPHQL_PATH	| (none)	| Path/URL for GraphQL service                          |

#### Example (application.properties)
```editorconfig
tryme.username=myuser
tryme.password=mypassword
tryme.auth.path=https://example.com/auth
tryme.graphql.path=https://example.com/graphql
```

#### Example (Environment Variables)
```editorconfig
export USERNAME=myuser
export PASSWORD=mypassword
export AUTH_PATH=https://example.com/auth
export GRAPHQL_PATH=https://example.com/graphql
```

## Running the Application

### Build the project:
```bash
mvn clean package
```

### Run the application:
```bash
mvn spring-boot:run
```

Access the API at http://localhost:8080/report