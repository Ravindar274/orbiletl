package com.orbitetl.workflow.repository;

import com.orbitetl.workflow.entity.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobExecutionRepository extends JpaRepository<JobExecution, UUID> {

    List<JobExecution> findByWorkflowIdOrderByCreatedAtDesc(UUID workflowId);

    List<JobExecution> findByStatus(String status);
}