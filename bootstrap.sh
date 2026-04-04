#!/bin/bash

echo "Creating Cheezapp project directory structure..."

# Base directory
mkdir -p cheezapp_

cd cheezapp

# Create Maven pom.xml
touch pom.xml

# Create main Java source structure
mkdir -p src/main/java/com/yenosoft/cheezapp

# Create sub-packages
mkdir -p src/main/java/com/yenosoft/cheezapp/config
mkdir -p src/main/java/com/yenosoft/cheezapp/domain/audit
mkdir -p src/main/java/com/yenosoft/cheezapp/repository
mkdir -p src/main/java/com/yenosoft/cheezapp/service/dto
mkdir -p src/main/java/com/yenosoft/cheezapp/controller
mkdir -p src/main/java/com/yenosoft/cheezapp/exception
mkdir -p src/main/java/com/yenosoft/cheezapp/security

# Create resources directory structure
mkdir -p src/main/resources/db/changelog

# Create placeholder files (empty files so directories are not ignored by git)

# Java files
touch src/main/java/com/yenosoft/cheezapp/CheezappApplication.java
touch src/main/java/com/yenosoft/cheezapp/config/JwtConfig.java
touch src/main/java/com/yenosoft/cheezapp/config/SecurityConfig.java
touch src/main/java/com/yenosoft/cheezapp/config/TenantContext.java
touch src/main/java/com/yenosoft/cheezapp/config/OpenApiConfig.java

touch src/main/java/com/yenosoft/cheezapp/domain/Tenant.java
touch src/main/java/com/yenosoft/cheezapp/domain/User.java
touch src/main/java/com/yenosoft/cheezapp/domain/ServiceProvider.java
touch src/main/java/com/yenosoft/cheezapp/domain/AvailabilitySlot.java
touch src/main/java/com/yenosoft/cheezapp/domain/Appointment.java
touch src/main/java/com/yenosoft/cheezapp/domain/audit/Auditable.java

touch src/main/java/com/yenosoft/cheezapp/repository/TenantAwareRepository.java
touch src/main/java/com/yenosoft/cheezapp/repository/TenantRepository.java
touch src/main/java/com/yenosoft/cheezapp/repository/ServiceProviderRepository.java
touch src/main/java/com/yenosoft/cheezapp/repository/AvailabilitySlotRepository.java
touch src/main/java/com/yenosoft/cheezapp/repository/AppointmentRepository.java

touch src/main/java/com/yenosoft/cheezapp/service/TenantService.java
touch src/main/java/com/yenosoft/cheezapp/service/AuthService.java
touch src/main/java/com/yenosoft/cheezapp/service/ServiceProviderService.java
touch src/main/java/com/yenosoft/cheezapp/service/BookingService.java

touch src/main/java/com/yenosoft/cheezapp/controller/TenantController.java
touch src/main/java/com/yenosoft/cheezapp/controller/AuthController.java
touch src/main/java/com/yenosoft/cheezapp/controller/ServiceProviderController.java
touch src/main/java/com/yenosoft/cheezapp/controller/AppointmentController.java

touch src/main/java/com/yenosoft/cheezapp/exception/GlobalExceptionHandler.java
touch src/main/java/com/yenosoft/cheezapp/exception/AppointmentConflictException.java
touch src/main/java/com/yenosoft/cheezapp/exception/OverbookingException.java

touch src/main/java/com/yenosoft/cheezapp/security/JwtService.java
touch src/main/java/com/yenosoft/cheezapp/security/JwtAuthenticationFilter.java

# Resources files
touch src/main/resources/application.yml
touch src/main/resources/db/changelog/db.changelog-master.yaml
touch src/main/resources/db/changelog/001-initial-schema.yaml
touch src/main/resources/logback-spring.xml

echo "✅ Cheezapp directory structure created successfully!"
echo "Project root: $(pwd)"
