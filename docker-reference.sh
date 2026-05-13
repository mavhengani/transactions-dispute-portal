#!/usr/bin/env bash

# Transactions Dispute Portal - Docker Quick Reference
# ====================================================

cat << 'EOF'
╔════════════════════════════════════════════════════════════════════════════╗
║           TRANSACTIONS DISPUTE PORTAL - DOCKER QUICK REFERENCE             ║
╚════════════════════════════════════════════════════════════════════════════╝

DOCUMENTATION
================
DOCKER.md              - Comprehensive Docker guide
README.md              - Project documentation
DOCKER_SETUP_SUMMARY.md - Setup summary

QUICK START
==============
Option 1 - Helper Script:        ./docker-start.sh
Option 2 - Make Command:         make up
Option 3 - Docker Compose:       docker-compose up -d --build

STOPPING SERVICES
====================
Option 1 - Helper Script:        ./docker-stop.sh
Option 2 - Make Command:         make down
Option 3 - Docker Compose:       docker-compose down

VIEWING LOGS
===============
Option 1 - Helper Script:        ./docker-logs.sh
Option 2 - Make Command:         make logs
Option 3 - All services:         docker-compose logs -f
Option 4 - Specific service:     docker-compose logs -f backend

USEFUL MAKE COMMANDS
=======================
make help              - Show all available commands
make build             - Build Docker images
make up                - Start services
make down              - Stop services
make restart           - Restart services
make logs              - View all logs
make ps                - Show running containers
make clean             - Remove everything
make shell-backend     - Open shell in backend
make shell-frontend    - Open shell in frontend
make shell-mysql       - Open MySQL CLI

SERVICE INFORMATION
======================
Frontend:              http://localhost:3000
Backend:               http://localhost:8080
Backend Health:        http://localhost:8080/actuator/health
Database:              localhost:3306

CONFIGURATION
================
.env                   - Environment variables (customize here)
.env.example           - Example configuration template

DOCKER COMPOSE COMMANDS
==========================
Build images:          docker-compose build --no-cache
Start services:        docker-compose up -d --build
Stop services:         docker-compose down
Remove volumes:        docker-compose down -v
Show containers:       docker-compose ps
View logs:             docker-compose logs -f

CONTAINER ACCESS
===================
Backend shell:         docker-compose exec backend bash
Frontend shell:        docker-compose exec frontend sh
MySQL shell:           docker-compose exec mysql mysql -u app_user -p

DEBUGGING
============
Backend logs:          docker-compose logs -f backend
Frontend logs:         docker-compose logs -f frontend
Database logs:         docker-compose logs -f mysql
Container stats:       docker stats
System disk usage:     docker system df

CLEANUP
==========
Remove containers:     docker-compose down
Remove volumes:        docker-compose down -v
Remove images:         docker-compose down --rmi all
System prune:          docker system prune

DEVELOPMENT MODE
===================
With hot reload:       docker-compose -f docker-compose.yml -f docker-compose.override.yml up

ENVIRONMENT VARIABLES
========================
Essential variables in .env:
  DB_PASSWORD          - Database root password
  DB_USER              - Database user
  DB_NAME              - Database name
  BACKEND_PORT         - Backend service port (default: 8080)
  FRONTEND_PORT        - Frontend service port (default: 3000)
  VITE_API_BASE_URL    - Frontend API endpoint

TROUBLESHOOTING
==================
Port already in use:   Change BACKEND_PORT, FRONTEND_PORT in .env
Database error:        Check MySQL health: docker-compose exec mysql mysql -u app_user -p
Can't connect:         Verify health: docker-compose ps, check logs: docker-compose logs
Clean rebuild:         docker-compose down -v && docker-compose build --no-cache && docker-compose up -d

VERIFICATION
===============
Verify setup:          ./verify-docker.sh

TYPICAL WORKFLOW
===================
1. Start services:     make up
2. Watch logs:         make logs (in another terminal)
3. Access frontend:    http://localhost:3000
4. Access API:         http://localhost:8080
5. Make changes:       Edit code
6. Rebuild:            docker-compose up -d --build
7. Stop services:      make down

TIPS & TRICKS
================
• Use 'make' for simple commands - easier to remember
• Use 'docker-compose logs -f' for real-time debugging
• Use docker-compose.override.yml for development settings
• Always check health: curl http://localhost:8080/actuator/health
• Keep .env file synced with .env.example for new variables

SUPPORT
==========
Read DOCKER.md for detailed information on any aspect
Check README.md for project-specific info
Review troubleshooting section in DOCKER.md for common issues

═══════════════════════════════════════════════════════════════════════════════

Ready to go! Start with: make up

═══════════════════════════════════════════════════════════════════════════════
EOF

