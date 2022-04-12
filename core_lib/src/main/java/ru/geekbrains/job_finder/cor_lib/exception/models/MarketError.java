package ru.geekbrains.job_finder.cor_lib.exception.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MarketError {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public MarketError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
