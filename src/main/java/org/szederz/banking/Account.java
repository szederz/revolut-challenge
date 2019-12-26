package org.szederz.banking;

public interface Account {
  AccountId getAccountId();

  Currency getBalance();

  Account withdraw(Currency amount);

  Account deposit(Currency amount);
}
