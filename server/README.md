# Bill Basher

Bill Basher is an application for splitting expenses.

## Installation and Running

1. Install PostgreSQL if not already installed.
2. Create a database in PostgreSQL named `Bill_Basher_DB`.
3. Create a `secrets.properties` file based on the example below and fill it with the data for connecting to the database and setting the JWT secret.
4. Run the application.

## Java Version and SDK

This project is built using Java version 17.0.10 and Amazon Corretto SDK.

## secrets.properties File

The `secrets.properties` file should contain the following settings:

spring.datasource.url=jdbc:postgresql://localhost:5432/Bill_Basher_DB
spring.datasource.username=your_database_username
spring.datasource.password=your_database_password
jwt.secret=your_jwt_secret

Please fill in `your_database_username`, `your_database_password`, and `your_jwt_secret` with your actual data.

## Usage

After running the application, you can use it to manage expenses. Additional information about available API endpoints can be found below.

## API Endpoints

### Events

- **Get Event by ID**: `GET /api/v1/events/{id}`
- **Update Event by ID**: `PUT /api/v1/events/{id}`
- **Get All Events**: `GET /api/v1/events`
- **Create Event**: `POST /api/v1/events`
- **Get Events List by User ID**: `GET /api/v1/events/list/{userId}`
- **Delete Event by ID**: `DELETE /api/v1/events/{id}`
- **Deactivate Event by ID**: `PUT /api/v1/events/deactivate/{id}`

### Expenses

- **Create Expense**: `POST /api/v1/expenses`
- **Get Expense by ID**: `GET /api/v1/expenses/{id}`
- **Update Expense by ID**: `PUT /api/v1/expenses/{id}`
- **Get Expenses List by Event ID**: `GET /api/v1/expenses/event/{id}`
- **Remove Expense by ID**: `DELETE /api/v1/expenses/remove/{id}`
- **Get Expenses by User ID and Event ID**: `GET /api/v1/expenses/{userId}/{eventId}`
- **Get All Expenses**: `GET /api/v1/expenses`

### Users

- **Get User by ID**: `GET /api/v1/users/{id}`
- **Update User by ID**: `PUT /api/v1/users/{id}`
- **Delete User by ID**: `DELETE /api/v1/users/{id}`
- **Get All Users**: `GET /api/v1/users`
- **Deactivate User by ID**: `PUT /api/v1/users/deactivate/{id}`
- **Get All Active Users**: `GET /api/v1/users/getAllActiveUsers`
- **Register New User**: `POST /api/v1/register`

### User Events

- **Add User to Event**: `POST /api/v1/user-event/add`
- **Remove User from Event**: `DELETE /api/v1/user-event/remove/{userId}/{eventId}`
- **Get Events List by User ID**: `GET /api/v1/events/by-user/{userId}`
- **Get Users List by Event ID**: `GET /api/v1/users/by-event/{eventId}`
- **Get Balance by Event ID**: `GET /api/v1/balance/{eventId}`


## View Code
Github repository ***[link](https://github.com/margaritabjerrum/bill--basher-project/tree/main/server/src)*** to view full code