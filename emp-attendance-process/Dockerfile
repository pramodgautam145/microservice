FROM openjdk:8-jdk-alpine
WORKDIR /attendance
COPY target/emp-attendance-process-0.0.1-SNAPSHOT.jar attendance.jar
# Install H2 database
RUN apt-get update && apt-get install -y h2
# Expose ports
EXPOSE 8082
EXPOSE 9092
# Set environment variables
ENV SPRING_DATASOURCE_URL=jdbc:h2:mem:attendance \
    SPRING_DATASOURCE_USERNAME=sa \
    SPRING_DATASOURCE_PASSWORD= \
    SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.h2.Driver \
    SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092 \
    SPRING_KAFKA_TOPIC=ATTENDANCE_EVENT

# Run the application
CMD ["java", "-jar", "attendance.jar"]


