package io.reflectoring.openfeign.services;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CalculatorHystrixFallback implements CalculatorService {

    @Override
    public Long add(Long firstNumber, Long secondNumber) {
        log.info("[Fallback add] Adding {} and {}", firstNumber, secondNumber);
        return firstNumber + secondNumber;
    }

    @Override
    public Long subtract(Long firstNumber, Long secondNumber) {
        return null;
    }

    @Override
    public Long multiply(Long firstNumber, Long secondNumber) {
        return null;
    }

    @Override
    public Long divide(Long firstNumber, Long secondNumber) {
        return null;
    }
}
