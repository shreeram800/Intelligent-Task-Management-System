# Use OpenJDK 21 base image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files
COPY . /app

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port (adjust if needed)
EXPOSE 8081

# Run the application
CMD ["java", "-jar", "target/user-service-0.0.1-SNAPSHOT.jar"]
