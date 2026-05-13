# Docker Configuration Guide

## Overview

This project is fully configured for Docker with multi-stage builds, health checks, environment variable management, and optimized production-ready configurations.

## Project Services

### 1. **MySQL Database**
- **Image**: mysql:8
- **Container**: transactions-mysql
- **Port**: 3306 (configurable via `DB_PORT`)
- **Features**:
  - Automatic health checks
  - Persistent volume storage
  - Environment-based configuration
  - Character set: UTF-8 (utf8mb4)

### 2. **Backend (Spring Boot)**
- **Framework**: Java 21 with Spring Boot 3.2.5
- **Port**: 8080 (configurable via `BACKEND_PORT`)
- **Features**:
  - Multi-stage Docker build (MavenAware build)
  - Spring Boot Actuator for health checks
  - Environment-based configuration
  - Health check endpoints: `/actuator/health`
  - Optimized JRE image (smallest footprint)
  - JMX monitoring enabled

### 3. **Frontend (React + Vite)**
- **Framework**: React 19 with Vite
- **Port**: 3000 (configurable via `FRONTEND_PORT`)
- **Features**:
  - Multi-stage Docker build with optimized production serving
  - Served with `serve` package for production
  - Environment variable support for API URL
  - Minimal image size with Alpine base

## Quick Start

### Prerequisites
- Docker 20.10+
- Docker Compose 2.0+
- Git

### Using Helper Scripts

```bash
# Start all services
./docker-start.sh

# View logs
./docker-logs.sh

# Stop all services
./docker-stop.sh
```

### Using Make Commands

```bash
# View all available commands
make help

# Build images
make build

# Start containers
make up

# Stop containers
make down

# View logs
make logs

# Open shell in containers
make shell-backend
make shell-frontend
make shell-mysql
```

### Using Docker Compose Directly

```bash
# Build and start all services
docker-compose up -d --build

# View logs from all services
docker-compose logs -f

# View logs from specific service
docker-compose logs -f backend

# Stop all services
docker-compose down

# Remove containers and volumes
docker-compose down -v

# View running containers
docker-compose ps
```

## Configuration

### Environment Variables

Create or modify `.env` file in the project root:

```env
# Database Configuration
DB_ROOT_PASSWORD=rotondwa
DB_NAME=transactions
DB_USER=app_user
DB_PASSWORD=app_password
DB_PORT=3306

# Backend Configuration
BACKEND_PORT=8080
JPA_DDL_AUTO=update        # Options: validate, update, create, create-drop, none
JPA_SHOW_SQL=false

# Frontend Configuration
FRONTEND_PORT=3000
VITE_API_BASE_URL=http://localhost:8080

# Java Options
JAVA_OPTS=-Xmx512m -Xms256m

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_TRANSACTIONS_DISPUTE_PORTAL=DEBUG
```

### Example Configuration

For **Production**:
```env
DB_PASSWORD=strong_secure_password
JPA_DDL_AUTO=validate
JPA_SHOW_SQL=false
VITE_API_BASE_URL=https://api.yourdomain.com
JAVA_OPTS=-Xmx1024m -Xms512m
```

For **Development**:
```env
DB_PASSWORD=dev_password
JPA_DDL_AUTO=update
JPA_SHOW_SQL=true
VITE_API_BASE_URL=http://localhost:8080
JAVA_OPTS=-Xmx512m -Xms256m
```

## Health Checks

### Backend Health Check
- **Endpoint**: `http://localhost:8080/actuator/health`
- **Status**: Available when all services are healthy
- **Interval**: 30 seconds
- **Timeout**: 10 seconds
- **Retries**: 3

### Frontend Health Check
- **Port**: 3000
- **Type**: HTTP GET
- **Interval**: 30 seconds
- **Timeout**: 10 seconds
- **Retries**: 3

### Database Health Check
- **Command**: MySQL ping
- **Interval**: 10 seconds
- **Timeout**: 5 seconds
- **Retries**: 5

## Docker Architecture

### Backend Build Process (Multi-Stage)

```
Stage 1: Builder
├─ Base: eclipse-temurin:21-jdk
├─ Install Maven dependencies
├─ Compile Java code
└─ Build JAR file

Stage 2: Runtime
├─ Base: eclipse-temurin:21-jre (smaller JRE only)
├─ Copy JAR from builder
├─ Install curl for health checks
└─ Run application
```

**Benefits**:
- Final image ~300MB (vs 500MB+ with full JDK)
- Faster startup time
- Better security (only runtime dependencies)

### Frontend Build Process (Multi-Stage)

```
Stage 1: Builder
├─ Base: node:18-alpine
├─ Install dependencies
├─ Build React/Vite app
└─ Generate optimized dist folder

Stage 2: Runtime
├─ Base: node:18-alpine
├─ Install serve package
├─ Copy optimized dist
└─ Serve production build
```

**Benefits**:
- Minimal final image size (~100MB)
- Production-optimized build
- Easy static file serving

## Docker Networking

All services communicate through an internal bridge network `app-network`:

```
┌─────────────────────────────────────────────────────┐
│                  app-network (bridge)               │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────┐ │
│  │  Frontend    │──│   Backend    │──│ MySQL    │ │
│  │  :3000       │  │   :8080      │  │  :3306   │ │
│  └──────────────┘  └──────────────┘  └──────────┘ │
│                                                     │
└─────────────────────────────────────────────────────┘
```

### Service Discovery
- **Frontend → Backend**: `http://backend:8080` (internal DNS)
- **Backend → Database**: `jdbc:mysql://mysql:3306/transactions` (internal DNS)

## Volumes and Persistence

### MySQL Data Volume
```yaml
mysql_data:
  - Path in Container: /var/lib/mysql
  - Persistence: Data survives container restart
  - Size: Grows as needed
```

### Logs Volume (Backend)
```yaml
./backend/logs:
  - Path in Container: /app/logs
  - Local Path: ./backend/logs
  - Accessible from host machine
```

## Troubleshooting

### Container won't start

```bash
# Check logs
docker-compose logs backend
docker-compose logs frontend
docker-compose logs mysql

# Check container status
docker-compose ps

# Inspect container
docker-compose exec backend sh
```

### Database connection error

```bash
# Verify database is healthy
docker-compose exec mysql mysql -u${DB_USER} -p${DB_PASSWORD} ${DB_NAME} -e "SELECT 1"

# Check network connectivity
docker-compose exec backend ping mysql
```

### Port conflicts

If ports are already in use:

```bash
# Option 1: Use different ports in .env
BACKEND_PORT=8081
FRONTEND_PORT=3001
DB_PORT=3307

# Option 2: Kill process using port (macOS)
lsof -ti:8080 | xargs kill -9
```

### Clean rebuild

```bash
# Remove everything and rebuild
docker-compose down -v
docker-compose build --no-cache
docker-compose up -d
```

### View application logs

```bash
# All services
docker-compose logs -f

# Following backend logs in real-time
docker-compose logs -f backend

# Last 50 lines of frontend logs
docker-compose logs --tail=50 frontend
```

## Development Workflow

### Local Development (with hot reload)

Frontend hot reload works automatically with Vite. For backend changes:

```bash
# 1. Make code changes
# 2. Rebuild container
docker-compose up -d --build backend

# 3. View logs
docker-compose logs -f backend
```

### Database Access

**From Host Machine**:
```bash
mysql -h localhost -u${DB_USER} -p${DB_PASSWORD} ${DB_NAME}
```

**From Container**:
```bash
docker-compose exec mysql mysql -u${DB_USER} -p${DB_PASSWORD} ${DB_NAME}
```

## Production Deployment

### Pre-deployment Checklist

```bash
# 1. Verify all tests pass
make build
docker-compose build --no-cache

# 2. Check configuration
cat .env

# 3. Start services
docker-compose up -d

# 4. Verify health checks
curl http://localhost:8080/actuator/health
curl http://localhost:3000

# 5. Test API endpoints
curl -X GET http://localhost:8080/api/transactions
```

### Scaling for Production

Modify `docker-compose.yml` for production:

```yaml
backend:
  # ... existing config ...
  deploy:
    replicas: 3
    resources:
      limits:
        cpus: '1'
        memory: 512M
      reservations:
        cpus: '0.5'
        memory: 256M

frontend:
  # ... existing config ...
  deploy:
    replicas: 2
    resources:
      limits:
        cpus: '0.5'
        memory: 256M
```

## Images and Registries

### Pushing to Docker Registry

```bash
# Tag images
docker tag transaction-backend:latest myregistry/transactions-backend:latest
docker tag transaction-frontend:latest myregistry/transactions-frontend:latest

# Push
docker push myregistry/transactions-backend:latest
docker push myregistry/transactions-frontend:latest
```

## Advanced Features

### JMX Monitoring
Backend includes JMX monitoring on port 9010 (not exposed by default).

To expose and monitor:
```yaml
# In docker-compose.yml backend service
ports:
  - "9010:9010"
```

### Custom Logging Configuration
Modify `backend/src/main/resources/application.yaml`:
```yaml
logging:
  level:
    root: DEBUG
    transactions_dispute_portal: TRACE
```

## Security Best Practices

**Implemented**:
- Environment variable secrets (not hardcoded)
- Network isolation with internal bridge network
- Health checks for container orchestration
- Non-root base images where possible
- Multi-stage builds (no build tools in production)

**Additional for Production**:
- Use secrets management (Docker Secrets, HashiCorp Vault)
- Enable SSL/TLS for database connections
- Use strong, randomly generated passwords
- Implement resource limits
- Set restrictive file permissions
- Use read-only filesystems where possible
- Implement log aggregation

## Useful Docker Commands

```bash
# View Docker resource usage
docker system df

# Clean up unused images/containers
docker system prune
docker system prune -a --volumes

# Inspect container
docker inspect transactions-backend

# Monitor container stats
docker stats

# Execute command in running container
docker-compose exec backend java -version

# Copy files from container
docker cp transactions-backend:/app/logs ./backend/logs

# View container events
docker events --filter "container=transactions-backend"
```

## Support Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Reference](https://docs.docker.com/compose/compose-file/)
- [Spring Boot Docker Support](https://spring.io/guides/gs/spring-boot-docker/)
- [React + Vite Docker Guide](https://vitejs.dev/)

---

**Last Updated**: 2026-05-13
**Version**: 1.0.0
**Maintainers**: Development Team

