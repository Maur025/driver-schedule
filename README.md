# kerno-schedule-api

Kerno-schedule-api is an API Service for managing and scheduling vehicles to transport.

## Setup

1. Clone the repository:
   ```bash
   git clone git@github.com:KernoTec/kerno-schedule-api.git
   ```
2. Navigate to the project directory:
   ```bash
   cd kerno-schedule-api
   ```
3. Install dependencies:
   ```bash
   mvn clean install
   ```

## Database Configuration

Before running the application, ensure that you have a PostgreSQL database set up. this project has
a docker-compose file, to 2 DB instances needed for the application.

1. Navigate to the `docker` directory:
   ```bash
   cd compose/database
   ```

You will see 2 directories, [`auth`](compose/database/auth/docker-compose.yml) and [
`app`](compose/database/app/docker-compose.yml) each
containing a `docker-compose.yml` file.They both
working same way, just different ports and DB names.

2. Navigate to either `auth` or `app` directory and run:
   ```bash
   docker-compose up -d
   ```

Default running ports are:

- Auth DB: `5541`
- App DB: `5540`

Default DB credentials you will find in [.env](compose/database/auth/.env) file
and [init-user-db.sh](compose/database/auth/init-user-db.sh)
script.

## Environment Variables

Copy the [local.env.example](local.env.example) to `local.env` and set the necessary environment
variables for your local setup.

Here a fast overview of important variables:

```Text
DB_HOST=localhost                                   # Database host for the application
DB_PORT=5540                                        # Database port for the application
DB_NAME=driver_schedule_db                          # Database name for the application
DB_USERNAME=postgres                                # Database username for the application
DB_PASSWORD=your-pass                               # Database password for the application

SERVER_PORT_APP=7801                                # Application server port
APP_SERVER_URL=http://localhost:7801                # Application server URL

# == AUTH CONFIG ==

DB_HOST_AUTH=localhost                              # Auth Database host
DB_PORT_AUTH=5541                                   # Auth Database port
DB_NAME_AUTH=driver_schedule_auth_db                # Auth Database name
DB_USERNAME_AUTH=postgres                           # Auth Database username
DB_PASSWORD_AUTH=your-pass                          # Auth Database password

SERVER_PORT_AUTH=7800                               # Auth server port
APP_SERVER_URL_AUTH=http://localhost:7800           # Auth server URL

OAUTH2_JWT_SECRET=your-jwt-secret                   # JWT secret for token signing with at least 32 characters
OAUTH2_JWT_ACCESS_TOKEN_EXP=4                       # Access token expiration time
OAUTH2_JWT_ACCESS_TOKEN_EXP_TYPE=hours              # Access token expiration time type (e.g., minutes, hours, days)
OAUTH2_JWT_REFRESH_TOKEN_EXP=7                      # Refresh token expiration time
OAUTH2_JWT_REFRESH_TOKEN_EXP_TYPE=days              # Refresh token expiration time type (e.g., minutes, hours, days)
OAUTH2_JWT_REFRESH_TOKEN_SECURE=dev                 # Set security values (e.g., dev, prod)
APP_CORS_ALLOWED_ORIGINS_AUTH=http://localhost:4200 # CORS allowed origins for auth server
OAUTH2_ADMIN_PASSWORD=admin-pass                    # Admin password for initial setup

MS_AUTH_SCHEME=http                                 # Auth microservice scheme (e.g., http, https)
MS_AUTH_HOST=172.20.50.60                           # Auth microservice host (same as auth host defined)
```
