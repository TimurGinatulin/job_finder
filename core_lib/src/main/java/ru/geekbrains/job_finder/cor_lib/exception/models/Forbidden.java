package ru.geekbrains.job_finder.cor_lib.exception.models;

public class Forbidden extends RuntimeException {
    public Forbidden(String message) {
        super(message);
    }
}
