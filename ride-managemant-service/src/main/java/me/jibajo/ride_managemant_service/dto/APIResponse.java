package me.jibajo.ride_managemant_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class APIResponse {
    private String message;
    private Object data;
}