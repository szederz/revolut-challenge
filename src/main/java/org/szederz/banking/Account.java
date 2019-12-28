package org.szederz.banking;

public interface Account extends Versioned {
  AccountId getAccountId();

  Currency getBalance();

  Account withdraw(Currency amount);

  Account deposit(Currency amount);
}
