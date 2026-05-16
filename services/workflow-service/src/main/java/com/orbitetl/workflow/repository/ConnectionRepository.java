package com.orbitetl.workflow.repository;

import com.orbitetl.workflow.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, UUID> {

    List<Connection> findByType(String type);

    boolean existsByName(String name);
}