package io.reflectoring.banking.service;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.reflectoring.banking.entity.Account;
import io.reflectoring.banking.utility.Checkers;

public class ClientInitializer {

	public static Function<List<Account>, Function> Initializer = (accountsList) -> {
		List<Account> accounts = accountsList.stream().collect(Collectors.toList());
		System.out.println(
				"1. Create account\n2. View Balance\n3. Withdraw amount\n4. Deposit amount\n5. Transfer money\n6. Get Account Details\n7. Show All Accounts\n");
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		switch (choice) {

		case 1:
			System.out.println("Enter name: ");
			String customerName = sc.next();
			System.out.println("Enter contact no: ");
			String contactNo = sc.next();
			System.out.println("Enter initial balance: ");
			Double balance = sc.nextDouble();
			accounts = (List<Account>) BankService.CreateAccount
					.apply(accounts, new Account(accounts.size() + 1, customerName, contactNo, balance)).get();
			System.out.println("Created bank account with Account Number " + accounts.size() + "\n\n");
			break;

		case 3:
			System.out.println("Enter account number: ");
			int accountNo = sc.nextInt();

			System.out.println("Enter withdrawal amount: ");
			Optional<Account> account1 = accounts.stream().filter(a -> a.getId() == accountNo)
					.collect(Collectors.toList()).size() > 0 ? Optional.ofNullable(
							accounts.stream().filter(a -> a.getId() == accountNo).collect(Collectors.toList()).get(0))
							: Optional.ofNullable(null);
			if (account1.isPresent()) {
				Optional<Account> account2 = BankService.WithdrawBalance.apply(account1.get(), sc.nextDouble(),
						Checkers.insufficientBalance);
				if (account2.isPresent())
					accounts.set(accounts.indexOf(account1.get()), account2.get());
				else
					System.out.println("Balance Insufficient\n\n");
			} else {
				System.out.println("That account does not exist.\n\n");
			}
			break;

		case 2:
			System.out.println("Enter account number: ");
			BankService.GetStringOutputForAccountCheck
					.apply(BankService.GetBalance.apply(accounts, sc.nextInt(), Checkers.accountExists))
					.ifPresent(System.out::println);
			break;

		case 4:
			System.out.println("Enter account number: ");
			int accountNo2 = sc.nextInt();
			Optional<Account> account4 = accounts.stream().filter(a -> a.getId() == accountNo2)
					.collect(Collectors.toList()).size() > 0 ? Optional.ofNullable(
							accounts.stream().filter(a -> a.getId() == accountNo2).collect(Collectors.toList()).get(0))
							: Optional.ofNullable(null);
			if (account4.isPresent()) {
				System.out.println("Enter deposit amount: ");
				double depositAmount = sc.nextDouble();
				Account account5 = BankService.DepositBalance.apply(account4.get(), depositAmount);
				accounts.set(accounts.indexOf(account4.get()), account5);
			} else {
				System.out.println("That account does not exist.\n\n");
			}
			break;

		case 5:
			System.out.println("Enter the first account number: ");
			int firstAccountNo = sc.nextInt();
			System.out.println("Enter the second account number: ");
			int secondAccountNo = sc.nextInt();
			Optional<Account> firstAccount = accounts.stream().filter(a -> a.getId() == firstAccountNo)
					.collect(Collectors.toList()).size() > 0
							? Optional.ofNullable(accounts.stream().filter(a -> a.getId() == firstAccountNo)
									.collect(Collectors.toList()).get(0))
							: Optional.ofNullable(null);
			Optional<Account> secondAccount = accounts.stream().filter(a -> a.getId() == secondAccountNo)
					.collect(Collectors.toList()).size() > 0
							? Optional.ofNullable(accounts.stream().filter(a -> a.getId() == secondAccountNo)
									.collect(Collectors.toList()).get(0))
							: Optional.ofNullable(null);
			if (firstAccount.isPresent() && secondAccount.isPresent()) {
				System.out.println("Enter withdrawal amount: ");
				double transferAmount = sc.nextDouble();
				Optional<Account> account2 = BankService.WithdrawBalance.apply(firstAccount.get(), transferAmount,
						Checkers.insufficientBalance);
				if (account2.get() != null) {
					accounts.set(accounts.indexOf(secondAccount.get()),
							BankService.DepositBalance.apply(secondAccount.get(), transferAmount));
					accounts.set(accounts.indexOf(firstAccount.get()), account2.get());
				} else
					System.out.println("Balance Insufficient\n\n");
			} else {
				System.out.println("Account numbers invalid\n\n");
			}
			break;

		case 6:
			System.out.println("Enter account no: ");
			BankService.GetStringOutputForAccountCheck
					.apply(BankService.GetDetails.apply(accounts, sc.nextInt(), Checkers.accountExists))
					.ifPresent(System.out::println);
			break;
		case 7:
			System.out.println("All accounts: \n");
			accounts.forEach(System.out::println);
		}
		return ClientInitializer.Initializer.apply(accounts);
	};
}
