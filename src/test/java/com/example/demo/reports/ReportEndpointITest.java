package com.example.demo.reports;

import com.example.demo.TestSecurityConfig;
import com.example.demo.service.PortfolioService;
import com.example.demo.service.dto.Security;
import com.example.demo.service.dto.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class ReportEndpointITest {
    @Autowired
    private WebTestClient testClient;

    @MockitoBean
    private PortfolioService service;

    @Test
    public void test_portfolio3ReturnsTransactionsAsCSVContent() {
        var transaction1 = new Transaction("123", "Security1", new Security("DE123"), "EUR", 12, 2.5, 12, "Equity", LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 1));
        var transaction2 = new Transaction("123", "Security2", new Security("DE456"), "EUR", 10, 2, 10, "Equity", LocalDate.of(2025, 1, 2), LocalDate.of(2025, 1, 2));

        given(service.getTransactions(eq(3L), any(), any())).willReturn(Flux.just(transaction1, transaction2));

        var body = testClient.get()
                .uri(builder -> builder
                        .path("/report")
                        .queryParam("portfolioId", 3)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=portfolio_transactions.csv")
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(body);
        Assertions.assertTrue(body.contains("Portfolio's shortName,Security name,Security ISIN code,Transaction's currency code,Amount,Unit price,Trade amount,Type name,Transaction date,Settlement date"));
        Assertions.assertTrue(body.contains("123,Security1,DE123,EUR,12,2.5,12,Equity,2025-01-01,2025-01-01"));
        Assertions.assertTrue(body.contains("123,Security2,DE456,EUR,10,2.0,10,Equity,2025-01-02,2025-01-02"));
    }
}
