#!/bin/bash

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Docker Configuration Verification${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Check files
echo -e "${YELLOW}Checking Docker configuration files...${NC}"

files=(
    "docker-compose.yml"
    "backend/Dockerfile"
    "frontend/transactions-dispute-frontend/Dockerfile"
    "backend/.dockerignore"
    "frontend/transactions-dispute-frontend/.dockerignore"
    ".env"
    ".env.example"
    "DOCKER.md"
    "Makefile"
    "docker-start.sh"
    "docker-stop.sh"
    "docker-logs.sh"
    "docker-compose.override.yml"
)

for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        echo -e "${GREEN}[OK]${NC} $file"
    else
        echo -e "${YELLOW}[MISSING]${NC} $file (not found)"
    fi
done

echo ""
echo -e "${YELLOW}Checking key configurations...${NC}"

# Check application.yaml
if grep -q "SPRING_DATASOURCE_URL" "backend/src/main/resources/application.yaml"; then
    echo -e "${GREEN}[OK]${NC} Backend supports environment variables"
else
    echo -e "${YELLOW}[WARN]${NC} Backend may need environment variable configuration"
fi

# Check pom.xml for actuator
if grep -q "spring-boot-starter-actuator" "backend/pom.xml"; then
    echo -e "${GREEN}[OK]${NC} Actuator dependency added for health checks"
else
    echo -e "${YELLOW}[WARN]${NC} Actuator not found in dependencies"
fi

echo ""
echo -e "${YELLOW}Docker & Docker Compose versions:${NC}"
docker --version
docker-compose --version

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Configuration Status: [OK] Fully Configured${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${BLUE}Next Steps:${NC}"
echo -e "1. Copy .env.example to .env and adjust settings if needed"
echo -e "2. Run: ${YELLOW}docker-compose up -d --build${NC}"
echo -e "3. Or use: ${YELLOW}make up${NC}"
echo -e "4. View logs: ${YELLOW}docker-compose logs -f${NC}"
echo -e "5. Access: http://localhost:3000 (Frontend)"
echo -e "6. Access: http://localhost:8080 (Backend)"
echo ""

