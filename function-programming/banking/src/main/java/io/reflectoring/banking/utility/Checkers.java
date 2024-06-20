package io.reflectoring.banking.utility;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import io.reflectoring.banking.entity.Account;

public class Checkers {
	
	
	public static BiPredicate<List<Account>, Integer> accountExists = (accounts,
			id) -> accounts.stream().filter(a -> a.getId() == id).collect(Collectors.toList()).size() > 0 ? true
					: false;
			
	public static BiPredicate<Account, Double> insufficientBalance = (account,
			withdrawalAmount) -> (account.getBalance() - withdrawalAmount) < 0 ? true : false;
			
	
}
