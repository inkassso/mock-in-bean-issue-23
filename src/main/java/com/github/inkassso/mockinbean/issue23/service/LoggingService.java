package com.github.inkassso.mockinbean.issue23.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * A service depending on {@link ProviderService}, which is injected by Spring.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class LoggingService {

    private final ProviderService providerService;

    public void logCurrentValue() {
        String value = providerService.provideValue();
        log.info("Value received: {}", value);
    }
}
