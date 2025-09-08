package com.example.demo.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class TrymeClientIT {

    @Autowired
    private Client client;

    @Test
    public void test_FetchPortfolio3() {
        final var query =
                """
                 query Test {
                        portfolio(id:3) {name}
                    }
                """;
        final var response = client.query(query, Map.of()).block();

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.field("portfolio").getValue());
    }
}
