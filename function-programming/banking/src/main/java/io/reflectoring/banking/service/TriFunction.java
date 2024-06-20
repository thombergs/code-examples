package io.reflectoring.banking.service;

@FunctionalInterface
public interface TriFunction<F, S, T, R> {
	public R apply(F first, S second, T third);
}

