package com.example.camunda.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("processApprovalDelegate")
@Slf4j
public class ProcessApprovalDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long requestId = (Long) execution.getVariable("requestId");
        String checker = (String) execution.getVariable("checker");
        
        log.info("======================================");
        log.info("Request APPROVED:");
        log.info("Request ID: {}", requestId);
        log.info("Checker: {}", checker);
        log.info("Process Instance ID: {}", execution.getProcessInstanceId());
        log.info("======================================");
        
        // Additional business logic can be added here
        // e.g., send notification, update external systems, etc.
    }
}
