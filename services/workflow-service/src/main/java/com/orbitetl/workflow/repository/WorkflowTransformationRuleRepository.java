package com.orbitetl.workflow.repository;

import com.orbitetl.workflow.entity.WorkflowTransformationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkflowTransformationRuleRepository
        extends JpaRepository<WorkflowTransformationRule, UUID> {

    void deleteByWorkflowId(UUID workflowId);
}