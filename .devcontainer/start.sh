#!/bin/bash
set -e

echo "==> Starting MobyGo backend..."
cd /workspaces/mobygo-car-rental/backend
mvn spring-boot:run -q &
BACKEND_PID=$!

echo "==> Waiting for backend on port 8080..."
until curl -sf http://localhost:8080/actuator/health > /dev/null 2>&1 || \
      curl -sf http://localhost:8080/api/cars > /dev/null 2>&1; do
  sleep 2
done

echo "==> Backend ready. Starting frontend on port 5500..."
cd /workspaces/mobygo-car-rental/frontend
python3 -m http.server 5500 &

echo ""
echo "  MobyGo is running!"
echo "  Backend API : http://localhost:8080/api"
echo "  Swagger UI  : http://localhost:8080/swagger-ui.html"
echo "  Frontend    : http://localhost:5500"
echo ""

wait $BACKEND_PID
