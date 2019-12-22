package org.szederz.banking.account;

import org.szederz.banking.Account;
import org.szederz.banking.AccountIdentifier;
import org.szederz.banking.Versionable;
import org.szederz.banking.currency.Currency;

public class LocalAccount implements Account, Versionable {
  private AccountIdentifier accountId;
  private Currency balance;
  private long version = 0L;

  public LocalAccount(AccountIdentifier accountId, Currency balance) {
    this.accountId = accountId;
    this.balance = balance;
  }

  public AccountIdentifier getAccountId() {
    return accountId;
  }

  @Override
  public LocalAccount withdraw(Currency amount) {
    return new LocalAccount(accountId, balance.minus(amount));
  }

  @Override
  public LocalAccount deposit(Currency amount) {
    return new LocalAccount(accountId, balance.plus(amount));
  }

  @Override
  public Currency getBalance() {
    return balance;
  }

  @Override
  public long getVersion() {
    return version;
  }
}
