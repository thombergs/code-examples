package io.reflectoring.banking.service;

@FunctionalInterface
public interface MultiFunction<F, S, T, Z, R> {
	public R apply(F first, S second, T third, Z fourth);
}