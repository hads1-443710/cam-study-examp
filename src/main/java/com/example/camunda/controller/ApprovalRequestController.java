package com.example.camunda.controller;

import com.example.camunda.dto.ApprovalDecisionDTO;
import com.example.camunda.dto.CreateRequestDTO;
import com.example.camunda.entity.ApprovalRequest;
import com.example.camunda.service.ApprovalRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@Tag(name = "Approval Requests", description = "API quản lý yêu cầu phê duyệt maker-checker")
public class ApprovalRequestController {

    private final ApprovalRequestService requestService;

    @PostMapping
    @Operation(
        summary = "Tạo yêu cầu phê duyệt mới",
        description = "Maker tạo một yêu cầu phê duyệt mới và khởi động quy trình Camunda"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Yêu cầu được tạo thành công",
                    content = @Content(schema = @Schema(implementation = ApprovalRequest.class))),
        @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ")
    })
    public ResponseEntity<ApprovalRequest> createRequest(
            @Parameter(description = "Thông tin yêu cầu phê duyệt", required = true)
            @Valid @RequestBody CreateRequestDTO dto) {
        ApprovalRequest request = requestService.createRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @PostMapping("/{id}/decision")
    @Operation(
        summary = "Phê duyệt hoặc từ chối yêu cầu",
        description = "Checker xử lý quyết định phê duyệt hoặc từ chối yêu cầu"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quyết định được xử lý thành công",
                    content = @Content(schema = @Schema(implementation = ApprovalRequest.class))),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy yêu cầu"),
        @ApiResponse(responseCode = "400", description = "Yêu cầu đã được xử lý hoặc dữ liệu không hợp lệ")
    })
    public ResponseEntity<ApprovalRequest> processDecision(
            @Parameter(description = "ID của yêu cầu", required = true)
            @PathVariable Long id,
            @Parameter(description = "Quyết định phê duyệt/từ chối", required = true)
            @Valid @RequestBody ApprovalDecisionDTO decision) {
        ApprovalRequest request = requestService.processDecision(id, decision);
        return ResponseEntity.ok(request);
    }

    @GetMapping
    @Operation(
        summary = "Lấy tất cả yêu cầu",
        description = "Trả về danh sách tất cả các yêu cầu phê duyệt"
    )
    @ApiResponse(responseCode = "200", description = "Danh sách yêu cầu được trả về thành công")
    public ResponseEntity<List<ApprovalRequest>> getAllRequests() {
        return ResponseEntity.ok(requestService.getAllRequests());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Lấy thông tin yêu cầu theo ID",
        description = "Trả về chi tiết một yêu cầu phê duyệt cụ thể"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Yêu cầu được tìm thấy",
                    content = @Content(schema = @Schema(implementation = ApprovalRequest.class))),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy yêu cầu")
    })
    public ResponseEntity<ApprovalRequest> getRequestById(
            @Parameter(description = "ID của yêu cầu", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(requestService.getRequestById(id));
    }

    @GetMapping("/maker/{maker}")
    @Operation(
        summary = "Lấy yêu cầu theo maker",
        description = "Trả về danh sách các yêu cầu của một maker cụ thể"
    )
    @ApiResponse(responseCode = "200", description = "Danh sách yêu cầu của maker")
    public ResponseEntity<List<ApprovalRequest>> getRequestsByMaker(
            @Parameter(description = "Tên của maker", required = true)
            @PathVariable String maker) {
        return ResponseEntity.ok(requestService.getRequestsByMaker(maker));
    }

    @GetMapping("/pending")
    @Operation(
        summary = "Lấy các yêu cầu đang chờ phê duyệt",
        description = "Trả về danh sách các yêu cầu có trạng thái PENDING"
    )
    @ApiResponse(responseCode = "200", description = "Danh sách yêu cầu đang chờ")
    public ResponseEntity<List<ApprovalRequest>> getPendingRequests() {
        return ResponseEntity.ok(requestService.getPendingRequests());
    }
}
