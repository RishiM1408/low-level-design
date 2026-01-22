# Build Stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Build all modules
RUN mvn clean package -DskipTests

# Runtime Stage
# Users can override the entrypoint to run specific modules
# Example: docker run -it lld-mastery chess
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/Problems/Chess/target/chess-*.jar ./chess.jar
COPY --from=build /app/Problems/VendingMachine/target/vending-machine-*.jar ./vending-machine.jar
COPY --from=build /app/Problems/ParkingLot/target/parking-lot-*.jar ./parking-lot.jar
COPY --from=build /app/Problems/RateLimiter/target/rate-limiter-*.jar ./rate-limiter.jar
# ... (Others would be mapped here in a real prod env, simplified for demo)

# Wrapper script to select module
COPY docker-entrypoint.sh .
RUN chmod +x docker-entrypoint.sh

ENTRYPOINT ["./docker-entrypoint.sh"]
