package com.example.camunda.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entity yêu cầu phê duyệt")
public class ApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID yêu cầu", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Tiêu đề yêu cầu", example = "Purchase Order Request")
    private String title;

    @Column(length = 1000)
    @Schema(description = "Mô tả chi tiết", example = "Request to purchase new equipment")
    private String description;

    @Column(nullable = false)
    @Schema(description = "Tên người tạo yêu cầu", example = "john.doe")
    private String maker;

    @Schema(description = "Tên người kiểm tra", example = "jane.smith")
    private String checker;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Trạng thái yêu cầu", example = "PENDING")
    private RequestStatus status = RequestStatus.PENDING;

    @Schema(description = "Lý do từ chối", example = "Insufficient budget")
    private String rejectionReason;

    @Column(name = "process_instance_id")
    @Schema(description = "ID của Camunda process instance")
    private String processInstanceId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Thời gian tạo")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Thời gian cập nhật")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum RequestStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
