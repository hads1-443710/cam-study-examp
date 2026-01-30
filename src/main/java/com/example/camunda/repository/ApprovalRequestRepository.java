package com.example.camunda.repository;

import com.example.camunda.entity.ApprovalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {
    
    List<ApprovalRequest> findByMaker(String maker);
    
    List<ApprovalRequest> findByStatus(ApprovalRequest.RequestStatus status);
    
    Optional<ApprovalRequest> findByProcessInstanceId(String processInstanceId);
}
