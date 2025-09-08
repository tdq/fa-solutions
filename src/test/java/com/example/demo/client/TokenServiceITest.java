package com.example.demo.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TokenServiceITest {

    @Autowired
    private TokenService tokenService;

    @Test
    public void test_getAccessToken() {
        final var accessToken = tokenService.getAccessToken().block();

        Assertions.assertNotNull(accessToken);
        Assertions.assertFalse(accessToken.isEmpty(), "Access token is empty");
    }
}
