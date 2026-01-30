package com.example.camunda.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO tạo yêu cầu phê duyệt mới")
public class CreateRequestDTO {
    
    @NotBlank(message = "Title is required")
    @Schema(description = "Tiêu đề yêu cầu", example = "Purchase Order Request", required = true)
    private String title;
    
    @Schema(description = "Mô tả chi tiết yêu cầu", example = "Request to purchase new equipment")
    private String description;
    
    @NotBlank(message = "Maker is required")
    @Schema(description = "Tên người tạo yêu cầu", example = "john.doe", required = true)
    private String maker;
}
