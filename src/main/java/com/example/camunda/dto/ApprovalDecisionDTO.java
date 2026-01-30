package com.example.camunda.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO quyết định phê duyệt/từ chối")
public class ApprovalDecisionDTO {
    
    @NotBlank(message = "Checker is required")
    @Schema(description = "Tên người kiểm tra", example = "jane.smith", required = true)
    private String checker;
    
    @Schema(description = "Phê duyệt (true) hoặc từ chối (false)", example = "true", required = true)
    private Boolean approved;
    
    @Schema(description = "Lý do từ chối (bắt buộc nếu approved = false)", example = "Insufficient budget")
    private String rejectionReason;
}
