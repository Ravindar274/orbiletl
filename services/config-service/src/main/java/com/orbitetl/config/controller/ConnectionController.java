package com.orbitetl.config.controller;

import com.orbitetl.config.dto.ConnectionRequestDto;
import com.orbitetl.config.dto.ConnectionResponseDto;
import com.orbitetl.config.service.ConnectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/connections")
@RequiredArgsConstructor
@Slf4j
public class ConnectionController {

    private final ConnectionService connectionService;

    @PostMapping
    public ResponseEntity<ConnectionResponseDto> createConnection(
            @Valid @RequestBody ConnectionRequestDto request) {
        ConnectionResponseDto response = connectionService.createConnection(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConnectionResponseDto> getConnectionById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(connectionService.getConnectionById(id));
    }

    @GetMapping
    public ResponseEntity<List<ConnectionResponseDto>> getAllConnections(
            @RequestParam(required = false) String type) {
        if (type != null) {
            return ResponseEntity.ok(connectionService.getConnectionsByType(type));
        }
        return ResponseEntity.ok(connectionService.getAllConnections());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConnectionResponseDto> updateConnection(
            @PathVariable UUID id,
            @Valid @RequestBody ConnectionRequestDto request) {
        return ResponseEntity.ok(connectionService.updateConnection(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConnection(@PathVariable UUID id) {
        connectionService.deleteConnection(id);
        return ResponseEntity.noContent().build();
    }
}