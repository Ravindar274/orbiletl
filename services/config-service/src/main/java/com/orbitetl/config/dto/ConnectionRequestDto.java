package com.orbitetl.config.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionRequestDto {

    @NotBlank(message = "Connection name is required")
    private String name;

    @NotBlank(message = "Connection type is required")
    private String type;

    private String host;
    private Integer port;
    private String databaseName;
    private String username;
    private String password;
    private String filePath;
    private String apiUrl;
}