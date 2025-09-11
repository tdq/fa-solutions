package com.example.demo.reports;

import com.example.demo.service.PortfolioService;
import com.example.demo.service.dto.TransactionCSV;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@RestController
public class ReportEndpoint {

    private final PortfolioService service;
    private final CsvMapper mapper = CsvMapper
            .builder()
            .addModule(new JavaTimeModule())
            .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
            .build();
    private final CsvSchema schema = mapper.schemaFor(TransactionCSV.class).withHeader();

    public ReportEndpoint(PortfolioService service) {
        this.service = service;
    }

    @GetMapping(path = "/report", produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<String> reportTransactions(
            @RequestParam("portfolioId") @Nonnull Long portfolioId,
            @RequestParam(value = "from", required = false) @Nullable LocalDate from,
            @RequestParam(value = "to", required = false) @Nullable LocalDate to,
            HttpServletResponse response) {
        Objects.requireNonNull(portfolioId, "Portfolio ID must be provided");

        final var transactions = service.getTransactions(portfolioId, from, to).collectList().block();

        final var body = Optional.ofNullable(transactions).orElse(Collections.emptyList())
                .stream()
                .map(transaction -> {
                    final var csvTransaction = TransactionCSV.fromTransaction(transaction);

                    try {
                        return mapper.writer(schema.withoutHeader()).writeValueAsString(csvTransaction);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });

        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=portfolio_transactions.csv");

        return Flux.fromStream(body).startWith(String.join(String.valueOf(schema.getColumnSeparator()), schema.getColumnNames()) + "\n");
    }
}
