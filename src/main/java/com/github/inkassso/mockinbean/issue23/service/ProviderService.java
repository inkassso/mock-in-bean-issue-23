package com.github.inkassso.mockinbean.issue23.service;

import org.springframework.stereotype.Service;

@Service
public class ProviderService {
    public String provideValue() {
        return "the actual value";
    }
}
