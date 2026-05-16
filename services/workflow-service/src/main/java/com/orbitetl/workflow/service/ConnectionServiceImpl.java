package com.orbitetl.workflow.service;

import com.orbitetl.workflow.dto.ConnectionRequestDto;
import com.orbitetl.workflow.dto.ConnectionResponseDto;
import com.orbitetl.workflow.entity.Connection;
import com.orbitetl.workflow.exception.ResourceNotFoundException;
import com.orbitetl.workflow.mapper.ConnectionMapper;
import com.orbitetl.workflow.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionServiceImpl implements ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final ConnectionMapper connectionMapper;

    @Override
    @Transactional
    public ConnectionResponseDto createConnection(ConnectionRequestDto request) {
        log.info("Creating connection with name: {}", request.getName());

        if (connectionRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException(
                    "Connection with name '" + request.getName() + "' already exists"
            );
        }

        Connection connection = connectionMapper.toEntity(request);
        Connection saved = connectionRepository.saveAndFlush(connection);

        log.info("Connection created successfully with id: {}", saved.getId());
        return connectionMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ConnectionResponseDto getConnectionById(UUID id) {
        log.info("Fetching connection with id: {}", id);

        Connection connection = connectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Connection not found with id: " + id
                ));

        return connectionMapper.toResponseDto(connection);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConnectionResponseDto> getAllConnections() {
        log.info("Fetching all connections");

        return connectionRepository.findAll()
                .stream()
                .map(connectionMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConnectionResponseDto> getConnectionsByType(String type) {
        log.info("Fetching connections by type: {}", type);

        return connectionRepository.findByType(type)
                .stream()
                .map(connectionMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public ConnectionResponseDto updateConnection(UUID id, ConnectionRequestDto request) {
        log.info("Updating connection with id: {}", id);

        Connection connection = connectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Connection not found with id: " + id
                ));

        connectionMapper.updateEntityFromDto(request, connection);
        Connection updated = connectionRepository.saveAndFlush(connection);

        log.info("Connection updated successfully with id: {}", updated.getId());
        return connectionMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteConnection(UUID id) {
        log.info("Deleting connection with id: {}", id);

        if (!connectionRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Connection not found with id: " + id
            );
        }

        connectionRepository.deleteById(id);
        log.info("Connection deleted successfully with id: {}", id);
    }
}