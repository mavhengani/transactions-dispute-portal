#!/bin/bash

# Stop and remove all containers
docker-compose down

# Optional: Remove volumes (uncomment to clear database on stop)
# docker-compose down -v

echo "[OK] Docker containers stopped and removed"

