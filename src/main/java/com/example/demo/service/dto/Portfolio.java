package com.example.demo.service.dto;

import java.util.List;

public record Portfolio(
        String shortName,
        List<Transaction> transactions) {
}
