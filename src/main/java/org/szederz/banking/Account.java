package org.szederz.banking;

import org.szederz.banking.currency.Currency;

public interface Account {
  AccountIdentifier getAccountIdentifier();

  Currency getBalance();

  Account withdraw(Currency amount);

  Account deposit(Currency amount);
}
