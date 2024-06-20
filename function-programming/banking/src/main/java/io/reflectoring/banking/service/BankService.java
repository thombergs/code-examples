package io.reflectoring.banking.service;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.reflectoring.banking.entity.Account;

public class BankService {

	public static BiFunction<List<Account>, Account, WeakReference<List<Account>>> CreateAccount = (List<Account> accountList,
			Account account) -> {
		List<Account> accountListNew = accountList.stream().collect(Collectors.toList());
		accountListNew.add(account);
		WeakReference<List<Account>> wRef = new WeakReference<>(accountListNew);
		return wRef;
	};

	public static BiFunction<Account, Double, Account> DepositBalance = (Account account, Double amount) -> new Account(
			account.getId(), account.getCustomerName(), account.getMobileNo(), account.getBalance() + amount);

	public static TriFunction<List<Account>, Integer, BiPredicate, Optional<Object>> GetBalance = (accounts, accountNo,
			accountCheckPredicate) -> (accountCheckPredicate.test(accounts, accountNo))
					? Optional.ofNullable(accounts.stream().filter(a -> a.getId() == accountNo)
							.collect(Collectors.toList()).get(0).getBalance())
					: Optional.ofNullable(null);

	public static TriFunction<Account, Double, BiPredicate, Optional<Account>> WithdrawBalance = (account, amount,
			balancePredicate) -> balancePredicate.test(account, amount) ? Optional.ofNullable(null)
					: Optional.ofNullable(new Account(account.getId(), account.getCustomerName(), account.getMobileNo(),
							account.getBalance() - amount));


	public static TriFunction<List<Account>, Integer, BiPredicate, Optional<Object>> GetDetails = (accounts, accountNo,
			accountCheckPredicate) -> !accountCheckPredicate.test(accounts, accountNo) ? Optional.ofNullable(null)
					: Optional.ofNullable(
							accounts.stream().filter(a -> a.getId() == accountNo).collect(Collectors.toList()).get(0));;

	public static Function<Optional<Object>, Optional<Object>> GetStringOutputForAccountCheck = (
			Optional<Object> opt) -> opt.isPresent() ? Optional.ofNullable(opt.get())
					: Optional.ofNullable("That acccount does not exist!");

	public static Consumer<List<Account>> ViewAllAccounts = accounts -> {
		accounts.forEach(a -> System.out.println(a));
	};
	
}
