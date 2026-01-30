package com.example.camunda.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("processRejectionDelegate")
@Slf4j
public class ProcessRejectionDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long requestId = (Long) execution.getVariable("requestId");
        String checker = (String) execution.getVariable("checker");
        String rejectionReason = (String) execution.getVariable("rejectionReason");
        
        log.info("======================================");
        log.info("Request REJECTED:");
        log.info("Request ID: {}", requestId);
        log.info("Checker: {}", checker);
        log.info("Reason: {}", rejectionReason);
        log.info("Process Instance ID: {}", execution.getProcessInstanceId());
        log.info("======================================");
        
        // Additional business logic can be added here
        // e.g., send notification to maker, log audit trail, etc.
    }
}
