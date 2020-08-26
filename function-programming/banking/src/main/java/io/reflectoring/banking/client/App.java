package io.reflectoring.banking.client;

import java.util.ArrayList;
import java.util.List;

import io.reflectoring.banking.entity.Account;
import io.reflectoring.banking.service.ClientInitializer;

public class App {
	public static void main(String[] args) {
		List<Account> accounts = new ArrayList<Account>();
		ClientInitializer.Initializer.apply(accounts);
	}
}
