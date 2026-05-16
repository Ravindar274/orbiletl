package com.orbitetl.config.service;

import com.orbitetl.config.dto.ConnectionRequestDto;
import com.orbitetl.config.dto.ConnectionResponseDto;

import java.util.List;
import java.util.UUID;

public interface ConnectionService {

    ConnectionResponseDto createConnection(ConnectionRequestDto request);

    ConnectionResponseDto getConnectionById(UUID id);

    List<ConnectionResponseDto> getAllConnections();

    List<ConnectionResponseDto> getConnectionsByType(String type);

    ConnectionResponseDto updateConnection(UUID id, ConnectionRequestDto request);

    void deleteConnection(UUID id);
}