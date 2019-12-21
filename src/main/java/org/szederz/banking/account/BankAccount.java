package org.szederz.banking.account;

import org.szederz.banking.currency.Currency;

public interface BankAccount {
  AccountNumber getAccountNumber();

  Currency getBalance();

  boolean hasAtLeast(Currency amount);

  BankAccount withdraw(Currency amount);

  BankAccount deposit(Currency amount);
}
