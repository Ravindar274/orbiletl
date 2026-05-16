package com.orbitetl.workflow.repository;


import com.orbitetl.workflow.entity.TransformationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransformationRuleRepository extends JpaRepository<TransformationRule, UUID> {

    List<TransformationRule> findByType(String type);

    boolean existsByName(String name);
}