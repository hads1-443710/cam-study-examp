package com.example.camunda.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("logRequestDelegate")
@Slf4j
public class LogRequestDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long requestId = (Long) execution.getVariable("requestId");
        String title = (String) execution.getVariable("title");
        String maker = (String) execution.getVariable("maker");
        
        log.info("======================================");
        log.info("New Approval Request Created:");
        log.info("Request ID: {}", requestId);
        log.info("Title: {}", title);
        log.info("Maker: {}", maker);
        log.info("Process Instance ID: {}", execution.getProcessInstanceId());
        log.info("======================================");
    }
}
