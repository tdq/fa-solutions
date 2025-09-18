package com.example.demo.reports;

import com.example.demo.service.PortfolioService;
import com.example.demo.service.dto.TransactionCSV;
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
import java.util.Objects;

@RestController
public class ReportEndpoint {

    private final PortfolioService service;
    private final CSVReport<TransactionCSV> csvReport = new CSVReport<>(TransactionCSV.class);

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

        final var transactions = service.getTransactions(portfolioId, from, to);
        final var body = transactions.map(TransactionCSV::fromTransaction);

        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=portfolio_transactions.csv");

        return csvReport.getReport(body);
    }
}
