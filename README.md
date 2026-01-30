# Camunda Approval Workflow Application

Ứng dụng Spring Boot với Camunda BPM để quản lý quy trình phê duyệt maker-checker.

## Tính năng

- **Maker (Người tạo)**: Tạo yêu cầu phê duyệt
- **Checker (Người kiểm tra)**: Phê duyệt hoặc từ chối yêu cầu
- **Quy trình Camunda**: Tự động hóa luồng phê duyệt
- **REST API**: Giao diện API đầy đủ
- **Camunda Cockpit**: Giao diện web để giám sát quy trình

## Yêu cầu

- Java 17+
- Maven 3.6+

## Cấu trúc dự án

```
src/
├── main/
│   ├── java/com/example/camunda/
│   │   ├── CamundaApprovalApplication.java    # Main application
│   │   ├── controller/
│   │   │   └── ApprovalRequestController.java # REST endpoints
│   │   ├── delegate/
│   │   │   ├── LogRequestDelegate.java        # Log request details
│   │   │   ├── ProcessApprovalDelegate.java   # Handle approval
│   │   │   └── ProcessRejectionDelegate.java  # Handle rejection
│   │   ├── dto/
│   │   │   ├── CreateRequestDTO.java          # DTO for creating request
│   │   │   └── ApprovalDecisionDTO.java       # DTO for approval decision
│   │   ├── entity/
│   │   │   └── ApprovalRequest.java           # JPA entity
│   │   ├── repository/
│   │   │   └── ApprovalRequestRepository.java # Data access
│   │   └── service/
│   │       └── ApprovalRequestService.java    # Business logic
│   └── resources/
│       ├── application.yml                     # Configuration
│       └── approval-process.bpmn              # BPMN workflow
└── pom.xml                                     # Maven dependencies
```

## Chạy ứng dụng

1. **Build project:**
   ```bash
   mvn clean install
   ```

2. **Chạy ứng dụng:**
   ```bash
   mvn spring-boot:run
   ```

3. **Truy cập:**
   - API: http://localhost:8080/api/requests
   - **Swagger UI**: http://localhost:8080/swagger-ui.html
   - **API Docs**: http://localhost:8080/api-docs
   - Camunda Cockpit: http://localhost:8080/camunda
   - H2 Console: http://localhost:8080/h2-console
   - Camunda Admin: username=`admin`, password=`admin`

## Swagger API Documentation

Ứng dụng tích hợp Swagger/OpenAPI để cung cấp tài liệu API tương tác:

- **Swagger UI**: Truy cập http://localhost:8080/swagger-ui.html để xem và test API trực tiếp
- **OpenAPI JSON**: Truy cập http://localhost:8080/api-docs để lấy specification JSON
- Tất cả endpoints đều có mô tả chi tiết, ví dụ, và schema validation

## API Endpoints

### 1. Tạo yêu cầu mới (Maker)

```bash
POST /api/requests
Content-Type: application/json

{
  "title": "Purchase Order Request",
  "description": "Request to purchase new equipment",
  "maker": "john.doe"
}
```

**Response:**
```json
{
  "id": 1,
  "title": "Purchase Order Request",
  "description": "Request to purchase new equipment",
  "maker": "john.doe",
  "checker": null,
  "status": "PENDING",
  "processInstanceId": "abc123",
  "createdAt": "2026-01-30T10:00:00",
  "updatedAt": "2026-01-30T10:00:00"
}
```

### 2. Phê duyệt/Từ chối yêu cầu (Checker)

```bash
POST /api/requests/1/decision
Content-Type: application/json

# Approve
{
  "checker": "jane.smith",
  "approved": true
}

# Reject
{
  "checker": "jane.smith",
  "approved": false,
  "rejectionReason": "Insufficient budget"
}
```

### 3. Lấy tất cả yêu cầu

```bash
GET /api/requests
```

### 4. Lấy yêu cầu theo ID

```bash
GET /api/requests/1
```

### 5. Lấy yêu cầu của maker

```bash
GET /api/requests/maker/john.doe
```

### 6. Lấy các yêu cầu đang chờ

```bash
GET /api/requests/pending
```

## Quy trình BPMN

Quy trình phê duyệt bao gồm các bước sau:

1. **Start Event**: Yêu cầu được tạo
2. **Log Request**: Ghi log thông tin yêu cầu
3. **Check Request**: User task cho checker
4. **Approved Gateway**: Quyết định dựa trên kết quả phê duyệt
5. **Process Approval**: Xử lý khi được phê duyệt
6. **Process Rejection**: Xử lý khi bị từ chối
7. **End Events**: Kết thúc quy trình

## Cấu hình

Cấu hình chính trong [application.yml](src/main/resources/application.yml):

- **Database**: H2 in-memory database
- **Camunda Admin**: username=`admin`, password=`admin`
- **Port**: 8080
- **History Level**: FULL

## Ví dụ sử dụng

### Kịch bản 1: Yêu cầu được phê duyệt

```bash
# 1. Maker tạo yêu cầu
curl -X POST http://localhost:8080/api/requests \
  -H "Content-Type: application/json" \
  -d '{
    "title": "New Equipment Request",
    "description": "Need new laptops",
    "maker": "alice"
  }'

# 2. Checker phê duyệt
curl -X POST http://localhost:8080/api/requests/1/decision \
  -H "Content-Type: application/json" \
  -d '{
    "checker": "bob",
    "approved": true
  }'
```

### Kịch bản 2: Yêu cầu bị từ chối

```bash
# 1. Maker tạo yêu cầu
curl -X POST http://localhost:8080/api/requests \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Travel Request",
    "description": "Business trip to Tokyo",
    "maker": "charlie"
  }'

# 2. Checker từ chối
curl -X POST http://localhost:8080/api/requests/2/decision \
  -H "Content-Type: application/json" \
  -d '{
    "checker": "bob",
    "approved": false,
    "rejectionReason": "Budget constraints"
  }'
```

## Monitoring với Camunda Cockpit

1. Truy cập http://localhost:8080/camunda
2. Đăng nhập với `admin/admin`
3. Xem các process instances đang chạy
4. Kiểm tra lịch sử và trạng thái của các task

## Database Schema

Table `approval_requests`:
- `id`: Primary key
- `title`: Tiêu đề yêu cầu
- `description`: Mô tả chi tiết
- `maker`: Người tạo yêu cầu
- `checker`: Người kiểm tra
- `status`: PENDING, APPROVED, REJECTED
- `rejection_reason`: Lý do từ chối (nếu có)
- `process_instance_id`: ID của Camunda process instance
- `created_at`: Thời gian tạo
- `updated_at`: Thời gian cập nhật

## Mở rộng

Có thể mở rộng ứng dụng với:

- **Authentication & Authorization**: Spring Security
- **Email Notifications**: Send emails on approval/rejection
- **File Attachments**: Support document uploads
- **Multi-level Approval**: Chain of approvers
- **Audit Trail**: Complete history tracking
- **External Database**: PostgreSQL, MySQL
- **Frontend**: React/Angular UI

## Troubleshooting

### Issue: Process definition not found
- Đảm bảo file `approval-process.bpmn` nằm trong `src/main/resources`
- Rebuild project với `mvn clean install`

### Issue: Task not found
- Kiểm tra xem process instance có đang chạy không
- Xem logs để biết chi tiết lỗi

### Issue: Database connection
- H2 database chạy in-memory, dữ liệu sẽ mất khi restart
- Xem H2 console tại http://localhost:8080/h2-console

## License

MIT License