package com.github.inkassso.mockinbean.issue23.service;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * For this aspect to intercept the call, Spring creates proxy for the {@link ProviderService},
 * which is stored in the context and gets injected into {@link LoggingService} and the Test class.
 */
@Aspect
@Component
@Slf4j
public class AnAspect {
    @Before("execution(* com.github.inkassso.mockinbean.issue23.service.ProviderService.provideValue())")
    public void logBeforeMethodExecution() {
        log.info("The value provider is being executed");
    }
}
