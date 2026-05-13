#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Transactions Dispute Portal - Docker${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "[ERROR] Docker is not installed. Please install Docker first."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "[ERROR] Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

echo -e "${GREEN}[OK] Docker and Docker Compose are installed${NC}"
echo ""

# Build and start containers
echo -e "${BLUE}Building Docker images and starting containers...${NC}"
docker-compose up -d --build

if [ $? -eq 0 ]; then
    echo -e "${GREEN}[OK] Docker containers started successfully!${NC}"
    echo ""
    echo -e "${BLUE}Service URLs:${NC}"
    echo -e "  - Frontend: http://localhost:3000"
    echo -e "  - Backend API: http://localhost:8080"
    echo -e "  - Database: localhost:3306"
    echo ""
    echo -e "${BLUE}Useful Commands:${NC}"
    echo -e "  docker-compose logs -f                 # View all logs"
    echo -e "  docker-compose logs -f backend         # View backend logs"
    echo -e "  docker-compose logs -f frontend        # View frontend logs"
    echo -e "  docker-compose down                    # Stop all containers"
    echo -e "  docker-compose ps                      # Show running containers"
    echo ""
else
    echo -e "${RED}[ERROR] Failed to start Docker containers${NC}"
    exit 1
fi

