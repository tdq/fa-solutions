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
import org.springframework.format.annotation.DateTimeFormat;
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

        // Service graphql endpoint is not allowing to fetch data as a stream. Have to fetch all data before processing
        // Also it allows to process errors before streaming data
        final var portfolio = service.getPortfolio(portfolioId, from, to).block();
        final var rows = portfolio.transactions().stream().map (transaction -> TransactionCSV.fromTransaction(portfolio.shortName(), transaction));

        final var body = rows.map(row -> {
                    try {
                        return mapper.writer(schema.withoutHeader()).writeValueAsString(row);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });

        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=portfolio_transactions.csv");

        return Flux.fromStream(body).startWith(String.join(String.valueOf(schema.getColumnSeparator()), schema.getColumnNames()) + "\n");
    }
}
