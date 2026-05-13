.PHONY: help build up down logs clean restart ps shell-backend shell-frontend shell-mysql

help:
	@echo "Transactions Dispute Portal - Docker Management"
	@echo "=============================================="
	@echo ""
	@echo "Available commands:"
	@echo "  make build              - Build Docker images"
	@echo "  make up                 - Start all containers"
	@echo "  make down               - Stop all containers"
	@echo "  make restart            - Restart all containers"
	@echo "  make logs               - View logs from all services"
	@echo "  make ps                 - Show running containers"
	@echo "  make clean              - Remove containers, images, and volumes"
	@echo "  make shell-backend      - Open shell in backend container"
	@echo "  make shell-frontend     - Open shell in frontend container"
	@echo "  make shell-mysql        - Open MySQL shell in database"
	@echo ""

build:
	@echo "Building Docker images..."
	docker-compose build --no-cache

up:
	@echo "Starting Docker containers..."
	docker-compose up -d
	@echo ""
	@echo "[OK] Services are running:"
	@echo "  - Frontend: http://localhost:3000"
	@echo "  - Backend: http://localhost:8080"
	@echo "  - Database: localhost:3306"

down:
	@echo "Stopping Docker containers..."
	docker-compose down

restart: down up
	@echo "Containers restarted"

logs:
	docker-compose logs -f

logs-backend:
	docker-compose logs -f backend

logs-frontend:
	docker-compose logs -f frontend

logs-mysql:
	docker-compose logs -f mysql

ps:
	docker-compose ps

clean:
	@echo "Cleaning up Docker resources..."
	docker-compose down -v
	@echo "[OK] Containers, volumes, and networks removed"

shell-backend:
	docker-compose exec backend bash

shell-frontend:
	docker-compose exec frontend sh

shell-mysql:
	docker-compose exec mysql mysql -p${DB_PASSWORD:-app_password} ${DB_NAME:-transactions}

rebuild: clean build up
	@echo "[OK] Clean rebuild complete"

status:
	@echo "Container Status:"
	@docker-compose ps
	@echo ""
	@echo "Disk Usage:"
	@docker system df

