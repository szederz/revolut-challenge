package org.szederz.banking;

import org.szederz.banking.currency.Currency;

public interface Account {
  AccountIdentifier getAccountId();

  Currency getBalance();

  Account withdraw(Currency amount);

  Account deposit(Currency amount);
}
