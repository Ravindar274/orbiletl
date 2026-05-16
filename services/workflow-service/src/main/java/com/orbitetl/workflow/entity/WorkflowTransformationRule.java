package com.orbitetl.workflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "workflow_transformation_rule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTransformationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transformation_rule_id", nullable = false)
    private TransformationRule transformationRule;

    @Column(name = "execution_order", nullable = false)
    private Integer executionOrder;
}