# Swagger API Documentation - Setup Complete

## Những gì đã được thêm vào

Đã tích hợp thành công Swagger/OpenAPI vào dự án với các file sau:

### 1. Dependencies (pom.xml)
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

### 2. OpenAPI Configuration
- File: `src/main/java/com/example/camunda/config/OpenApiConfig.java`
- Cấu hình metadata cho API documentation

### 3. Swagger UI Configuration (application.yml)
```yaml
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
```

### 4. Controller Annotations
- Đã thêm `@Tag`, `@Operation`, `@Parameter`, `@ApiResponse` vào `ApprovalRequestController.java`
- Tất cả endpoints đều có mô tả tiếng Việt chi tiết

### 5. DTO và Entity Annotations  
- Đã thêm `@Schema` annotations vào:
  - `CreateRequestDTO.java`
  - `ApprovalDecisionDTO.java`
  - `ApprovalRequest.java`

## Lưu ý về Build Issue

**Vấn đề hiện tại**: Project đang sử dụng Java 25, nhưng Lombok có vấn đề tương thích. Để build thành công:

### Giải pháp 1: Sử dụng Java 17
```bash
# Install SDKMAN
curl -s "https://get.sdkman.io" | bash

# Install Java 17
sdk install java 17.0.9-tem

# Set Java 17
sdk use java 17.0.9-tem

# Build
mvn clean package
```

### Giải pháp 2: Sử dụng Docker
Tạo file `Dockerfile`:
```dockerfile
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build và chạy:
```bash
docker build -t camunda-approval .
docker run -p 8080:8080 camunda-approval
```

### Giải pháp 3: Update Dev Container
Thêm vào `.devcontainer/devcontainer.json`:
```json
{
  "image": "mcr.microsoft.com/devcontainers/java:17",
  "features": {
    "ghcr.io/devcontainers/features/java:1": {
      "version": "17"
    }
  }
}
```

## Truy cập Swagger UI

Sau khi build và chạy thành công:

1. **Swagger UI**: http://localhost:8080/swagger-ui.html
2. **API Docs JSON**: http://localhost:8080/api-docs
3. **API Docs YAML**: http://localhost:8080/api-docs.yaml

## Tính năng Swagger đã implement

✅ API Documentation tự động  
✅ Interactive API testing  
✅ Request/Response schemas  
✅ Validation rules hiển thị  
✅ Mô tả tiếng Việt đầy đủ  
✅ Example values cho tất cả fields  
✅ HTTP status codes documentation  
✅ Grouped by tags  

## Test Swagger

Sau khi ứng dụng chạy, bạn có thể:

1. Mở Swagger UI tại http://localhost:8080/swagger-ui.html
2. Expand "Approval Requests" tag
3. Thử các endpoints:
   - POST `/api/requests` - Tạo request mới
   - POST `/api/requests/{id}/decision` - Phê duyệt/từ chối
   - GET `/api/requests` - Lấy tất cả requests
   - GET `/api/requests/pending` - Lấy pending requests

## Code đã hoàn thành

Tất cả code cho Swagger đã được implement đầy đủ. Chỉ cần fix vấn đề Java version để build thành công.

Các file đã được tạo/cập nhật:
- [x] pom.xml (dependencies)
- [x] application.yml (configuration)
- [x] OpenApiConfig.java (OpenAPI config)
- [x] ApprovalRequestController.java (annotations)
- [x] CreateRequestDTO.java (schema annotations)
- [x] ApprovalDecisionDTO.java (schema annotations)
- [x] ApprovalRequest.java (schema annotations)
- [x] README.md (documentation)
