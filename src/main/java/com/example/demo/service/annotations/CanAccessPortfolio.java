package com.example.demo.service.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize("@checkPortfolioAccess.canGetInformation(authentication, #portfolioId)")
public @interface CanAccessPortfolio {
}
