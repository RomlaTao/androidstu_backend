#!/bin/bash

# Test Services Connectivity Script
echo "======================================"
echo "Testing Services Connectivity"
echo "======================================"

# Function to test service health
test_service() {
    local service_name=$1
    local url=$2
    local max_retries=5
    local retry_count=0
    local wait_time=10
    
    echo "Testing $service_name..."
    
    while [ $retry_count -lt $max_retries ]; do
        response=$(curl -s -o /dev/null -w "%{http_code}" $url)
        
        if [ $response -eq 200 ]; then
            echo "✅ $service_name is healthy"
            return 0
        else
            echo "⚠️ Attempt $(($retry_count + 1))/$max_retries: $service_name returned HTTP $response"
            retry_count=$((retry_count + 1))
            if [ $retry_count -lt $max_retries ]; then
                echo "Waiting ${wait_time} seconds before next attempt..."
                sleep $wait_time
            fi
        fi
    done
    
    echo "❌ $service_name is not responding after $max_retries attempts"
    return 1
}

# Function to check Eureka registration
check_eureka_registration() {
    local service_name=$1
    local max_retries=5
    local retry_count=0
    local wait_time=10
    
    echo "Checking $service_name registration in Eureka..."
    
    while [ $retry_count -lt $max_retries ]; do
        response=$(curl -s http://localhost:8761/eureka/apps/$service_name)
        if echo $response | grep -q "<name>$service_name</name>"; then
            echo "✅ $service_name is registered with Eureka"
            return 0
        else
            echo "⚠️ Attempt $(($retry_count + 1))/$max_retries: $service_name not found in Eureka"
            retry_count=$((retry_count + 1))
            if [ $retry_count -lt $max_retries ]; then
                echo "Waiting ${wait_time} seconds before next attempt..."
                sleep $wait_time
            fi
        fi
    done
    
    echo "❌ $service_name failed to register with Eureka after $max_retries attempts"
    return 1
}

# Wait for initial startup
echo "Waiting 60 seconds for services to initialize..."
sleep 60

# Test Discovery Server
test_service "Discovery Server" "http://localhost:8761/actuator/health"

# Test and verify registration for each service in sequence
services=(
    "APISERVICE|8080"
    "AUTHSERVICE|8005"
    "USERSERVICE|8006"
    "WORKOUTSERVICE|8007"
    "MEALSERVICE|8008"
    "ANALYSTSERVICE|8009"
)

for service in "${services[@]}"; do
    IFS="|" read -r name port <<< "$service"
    echo ""
    echo "Testing $name..."
    test_service "$name" "http://localhost:$port/actuator/health"
    check_eureka_registration "$name"
    # Wait between services
    sleep 10
done

echo ""
echo "======================================"
echo "Testing Inter-Service Communication"
echo "======================================"

# Test API Gateway routes
echo "Testing routes through API Gateway..."

endpoints=(
    "auth/actuator/health|Auth Service"
    "users/actuator/health|User Service"
    "workouts/actuator/health|Workout Service"
    "meals/actuator/health|Meal Service"
    "analytics/actuator/health|Analyst Service"
)

for endpoint in "${endpoints[@]}"; do
    IFS="|" read -r path service_name <<< "$endpoint"
    echo ""
    echo "Testing $service_name via Gateway..."
    response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/$path)
    if [ $response -eq 200 ]; then
        echo "✅ $service_name is accessible through Gateway"
    else
        echo "❌ $service_name is not accessible through Gateway (HTTP $response)"
    fi
done

echo ""
echo "======================================"
echo "Service Dependencies Check"
echo "======================================"

# Check Redis connection
echo "Testing Redis connection..."
if nc -z localhost 6379; then
    echo "✅ Redis is accessible"
else
    echo "❌ Redis is not accessible"
fi

# Check MySQL connections
databases=(
    "3307|Auth DB"
    "3308|User DB"
    "3309|Workout DB"
    "3310|Meal DB"
    "3311|Analyst DB"
)

for db in "${databases[@]}"; do
    IFS="|" read -r port name <<< "$db"
    echo "Testing $name connection..."
    if nc -z localhost $port; then
        echo "✅ $name is accessible"
    else
        echo "❌ $name is not accessible"
    fi
done

echo ""
echo "Test completed!" 