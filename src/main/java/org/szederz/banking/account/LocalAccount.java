package org.szederz.banking.account;

import org.szederz.banking.Account;
import org.szederz.banking.account.identifier.AccountNumber;
import org.szederz.banking.currency.Currency;
import org.szederz.banking.currency.LocalCurrency;

public class LocalAccount implements Account {
  private AccountNumber accountNumber;
  private Currency balance;

  public LocalAccount(AccountNumber accountNumber) {
    this(accountNumber, new LocalCurrency(0));
  }

  public LocalAccount(AccountNumber accountNumber, Currency balance) {
    this.accountNumber = accountNumber;
    this.balance = balance;
  }

  public AccountNumber getAccountIdentifier() {
    return accountNumber;
  }

  @Override
  public Account withdraw(Currency amount) {
    return new LocalAccount(accountNumber, balance.minus(amount));
  }

  @Override
  public Account deposit(Currency amount) {
    return new LocalAccount(accountNumber, balance.plus(amount));
  }

  public LocalAccount withBalance(Currency balance) {
    this.balance = balance;

    return this;
  }

  @Override
  public Currency getBalance() {
    return balance;
  }
}
