package com.example.demo.service.dto;

import jakarta.annotation.Nonnull;

import java.util.List;

public record Portfolio(
        @Nonnull String shortName,
        @Nonnull List<Transaction> transactions) {
}
