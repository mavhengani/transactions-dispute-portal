# Transactions Dispute Portal

A full-stack transaction dispute management system with fully containerized Docker setup.

## Tech Stack

- **Backend**: Java 21, Spring Boot 3.2.5, Spring Security, JWT
- **Frontend**: React 19, Vite, React Router
- **Database**: MySQL 8
- **Container**: Docker & Docker Compose with multi-stage builds
- **Architecture**: Microservices-ready with service discovery

## Features

- User registration & login with JWT authentication
- View transactions
- Dispute transactions
- Filter disputed/non-disputed
- Transaction history & analytics
- Role-based access control
- Docker containerized deployment

## Quick Start

### Prerequisites
- Docker 20.10+
- Docker Compose 2.0+
- Git

### Option 1: Using Helper Scripts

```bash
# Make scripts executable
chmod +x docker-start.sh docker-stop.sh docker-logs.sh

# Start services
./docker-start.sh

# View logs
./docker-logs.sh

# Stop services
./docker-stop.sh
```

### Option 2: Using Make

```bash
# View all commands
make help

# Build and start
make up

# View logs
make logs

# Stop
make down
```

### Option 3: Using Docker Compose Directly

```bash
# Build and start all services
docker-compose up -d --build

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

## Service URLs

After starting with `docker-compose up`:

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **Database**: localhost:3306
- **Health Check (Backend)**: http://localhost:8080/actuator/health

## Configuration

### Environment Variables

Copy `.env.example` to `.env` and customize:

```bash
cp .env.example .env
```

Key variables:
- `DB_PASSWORD`: Database password
- `BACKEND_PORT`: Backend service port (default: 8080)
- `FRONTEND_PORT`: Frontend service port (default: 3000)
- `VITE_API_BASE_URL`: Frontend API endpoint
- `JPA_DDL_AUTO`: Hibernate DDL mode (validate, update, create, create-drop)

See **[DOCKER.md](./DOCKER.md)** for complete configuration guide.

## API Endpoints

### Authentication
```
POST /api/auth/register      - Register new user
POST /api/auth/login         - Login user
POST /api/auth/refresh       - Refresh JWT token
```

### Transactions
```
GET    /api/transactions             - Get all transactions
GET    /api/transactions/{id}        - Get transaction by ID
PUT    /api/transactions/{id}/dispute - Dispute a transaction
GET    /api/transactions/status/{status} - Filter by status
```

### Health & Metrics
```
GET    /actuator/health      - Service health check
GET    /actuator/info        - Application info
GET    /actuator/metrics     - Application metrics
```

## Docker Architecture

### Backend (Multi-stage Build)
- **Builder Stage**: Maven build with full JDK (eclipse-temurin:21-jdk)
- **Runtime Stage**: Optimized JRE image (eclipse-temurin:21-jre)
- **Result**: ~300MB final image
- **Features**: Health checks, Spring Boot Actuator, JMX debugging

### Frontend (Multi-stage Build)
- **Builder Stage**: Node.js with npm build (node:18-alpine)
- **Runtime Stage**: Alpine Linux with serve package
- **Result**: ~100MB final image
- **Features**: Production-optimized build, health checks

### Database
- **Image**: MySQL 8 with UTF-8 configuration
- **Persistence**: Named volume for data durability
- **Health Checks**: Automated monitoring

## Development

### Local Development

For development with hot reload and debugging:

```bash
# Use the override compose file
docker-compose -f docker-compose.yml -f docker-compose.override.yml up
```

Features:
- Backend with Spring DevTools support
- Frontend with Vite hot reload
- Verbose logging
- JMX debug port (9010)

### Building Images

```bash
# Build without cache (clean build)
docker-compose build --no-cache

# Build specific service
docker-compose build backend
docker-compose build frontend
```

### Accessing Services

```bash
# Shell into running container
docker-compose exec backend bash
docker-compose exec frontend sh
docker-compose exec mysql bash

# View logs
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql

# Inspect container
docker-compose exec backend java -version
```

## Testing

### Unit Tests

Backend:
```bash
docker-compose exec backend ./mvnw test
```

Frontend:
```bash
docker-compose exec frontend npm test
```

### Integration Tests

```bash
# Run with full containers
docker-compose up -d
docker-compose exec backend ./mvnw verify
docker-compose exec frontend npm run test
```

### Health Checks

```bash
# Backend health
curl http://localhost:8080/actuator/health

# Frontend health
curl http://localhost:3000

# Database health
docker-compose exec mysql mysql -u${DB_USER} -p${DB_PASSWORD} ${DB_NAME} -e "SELECT 1"
```

## Database

### Access MySQL

From host:
```bash
mysql -h localhost -u app_user -p -D transactions
```

From within container:
```bash
docker-compose exec mysql mysql -u app_user -p -D transactions
```

### Database Initialization

The database is automatically initialized with:
- UTF-8 character set (utf8mb4)
- Automatic schema creation (Hibernate DDL-auto)
- Connection pooling (HikariCP)

### Backup & Restore

```bash
# Backup
docker-compose exec mysql mysqldump -u app_user -p transactions > backup.sql

# Restore
docker-compose exec mysql mysql -u app_user -p transactions < backup.sql
```

## Production Deployment

### Pre-deployment

1. **Review Configuration**
   ```bash
   cat .env
   ```

2. **Test Build**
   ```bash
   docker-compose build --no-cache
   ```

3. **Verify Services**
   ```bash
   docker-compose up -d
   docker-compose ps
   ```

4. **Check Health**
   ```bash
   curl http://localhost:8080/actuator/health
   ```

### Deployment Recommendations

- Use strong, random database passwords
- Set `JPA_DDL_AUTO=validate` for production
- Disable `JPA_SHOW_SQL` in production
- Configure appropriate `JAVA_OPTS` for memory
- Implement log aggregation
- Use Docker secrets or environment management
- Enable HTTPS/SSL
- Set resource limits
- Monitor container health

See **[DOCKER.md](./DOCKER.md)** for advanced production configuration.

## Troubleshooting

### Services won't start

```bash
# Check logs
docker-compose logs backend
docker-compose logs frontend
docker-compose logs mysql

# Verify status
docker-compose ps
```

### Port already in use

Modify `.env`:
```
BACKEND_PORT=8081
FRONTEND_PORT=3001
DB_PORT=3307
```

### Database connection issues

```bash
# Test connectivity
docker-compose exec backend ping mysql

# Verify credentials
docker-compose exec mysql mysql -u app_user -p app_password -e "SELECT 1"
```

### Clean rebuild

```bash
docker-compose down -v
docker-compose build --no-cache
docker-compose up -d
```

## Useful Commands

```bash
# View all available commands
make help

# Common operations
make build          # Build images
make up             # Start services
make down           # Stop services
make logs           # View logs
make clean          # Remove everything
make restart        # Restart services
make shell-backend  # Shell into backend
make shell-mysql    # MySQL CLI
```

## Docker Commands Reference

```bash
# Compose commands
docker-compose build               # Build images
docker-compose up -d               # Start services
docker-compose down                # Stop services
docker-compose logs -f             # View logs
docker-compose ps                  # Show containers
docker-compose exec <svc> bash     # Open shell

# System commands
docker system df                   # Disk usage
docker system prune                # Clean up unused
docker stats                       # Monitor resources
docker images                      # List images
docker ps -a                       # List all containers
```

## Documentation

- **[DOCKER.md](./DOCKER.md)** - Comprehensive Docker configuration guide
- **[HELP.md](./backend/HELP.md)** - Spring Boot project help
- **[AGENTS.md](./backend/AGENTS.md)** - Agent-specific documentation

## Project Structure

```
transactions-dispute-portal/
├── backend/                      # Spring Boot application
│   ├── src/
│   │   ├── main/java/           # Source code
│   │   ├── main/resources/      # Configuration files
│   │   └── test/                # Unit tests
│   ├── Dockerfile               # Multi-stage build
│   ├── .dockerignore            # Docker exclusions
│   └── pom.xml                  # Maven configuration
├── frontend/                    # React + Vite application
│   └── transactions-dispute-frontend/
│       ├── src/                 # React source code
│       ├── Dockerfile           # Multi-stage build
│       ├── .dockerignore        # Docker exclusions
│       └── vite.config.js       # Vite configuration
├── docker-compose.yml           # Service orchestration
├── docker-compose.override.yml  # Development overrides
├── .env                         # Environment variables
├── .env.example                 # Example variables
├── Makefile                     # Quick commands
├── DOCKER.md                    # Docker guide
└── README.md                    # This file
```

## Support & Contributing

For issues or questions:
1. Check [DOCKER.md](./DOCKER.md) troubleshooting section
2. Review Docker documentation
3. Check container logs: `docker-compose logs`

## License

[Your License Here]

---

**Last Updated**: 2026-05-13  
**Status**: Fully Docker Configured  
**Next Steps**: Start with `docker-compose up -d --build`
