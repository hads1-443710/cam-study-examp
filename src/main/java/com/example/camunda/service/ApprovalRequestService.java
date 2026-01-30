package com.example.camunda.service;

import com.example.camunda.dto.ApprovalDecisionDTO;
import com.example.camunda.dto.CreateRequestDTO;
import com.example.camunda.entity.ApprovalRequest;
import com.example.camunda.repository.ApprovalRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalRequestService {

    private final ApprovalRequestRepository requestRepository;
    private final RuntimeService runtimeService;
    private final TaskService taskService;

    @Transactional
    public ApprovalRequest createRequest(CreateRequestDTO dto) {
        // Create approval request entity
        ApprovalRequest request = new ApprovalRequest();
        request.setTitle(dto.getTitle());
        request.setDescription(dto.getDescription());
        request.setMaker(dto.getMaker());
        request.setStatus(ApprovalRequest.RequestStatus.PENDING);
        
        request = requestRepository.save(request);
        
        // Start Camunda process
        Map<String, Object> variables = new HashMap<>();
        variables.put("requestId", request.getId());
        variables.put("title", request.getTitle());
        variables.put("maker", request.getMaker());
        
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
            "approval-process",
            request.getId().toString(),
            variables
        );
        
        // Update request with process instance ID
        request.setProcessInstanceId(processInstance.getId());
        request = requestRepository.save(request);
        
        log.info("Created approval request {} with process instance {}", 
                request.getId(), processInstance.getId());
        
        return request;
    }

    @Transactional
    public ApprovalRequest processDecision(Long requestId, ApprovalDecisionDTO decision) {
        ApprovalRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));
        
        if (request.getStatus() != ApprovalRequest.RequestStatus.PENDING) {
            throw new RuntimeException("Request has already been processed");
        }
        
        // Find the user task for this request
        Task task = taskService.createTaskQuery()
            .processInstanceId(request.getProcessInstanceId())
            .taskDefinitionKey("check-request")
            .singleResult();
        
        if (task == null) {
            throw new RuntimeException("No pending task found for this request");
        }
        
        // Update request
        request.setChecker(decision.getChecker());
        
        // Complete task with decision
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", decision.getApproved());
        variables.put("checker", decision.getChecker());
        
        if (Boolean.FALSE.equals(decision.getApproved())) {
            request.setStatus(ApprovalRequest.RequestStatus.REJECTED);
            request.setRejectionReason(decision.getRejectionReason());
            variables.put("rejectionReason", decision.getRejectionReason());
        } else {
            request.setStatus(ApprovalRequest.RequestStatus.APPROVED);
        }
        
        taskService.complete(task.getId(), variables);
        request = requestRepository.save(request);
        
        log.info("Processed request {} with decision: {}", requestId, decision.getApproved());
        
        return request;
    }

    public List<ApprovalRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    public ApprovalRequest getRequestById(Long id) {
        return requestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found: " + id));
    }

    public List<ApprovalRequest> getRequestsByMaker(String maker) {
        return requestRepository.findByMaker(maker);
    }

    public List<ApprovalRequest> getPendingRequests() {
        return requestRepository.findByStatus(ApprovalRequest.RequestStatus.PENDING);
    }
}
