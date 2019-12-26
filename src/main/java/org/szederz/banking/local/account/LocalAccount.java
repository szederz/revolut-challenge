package org.szederz.banking.local.account;

import org.szederz.banking.Account;
import org.szederz.banking.AccountId;
import org.szederz.banking.Currency;
import org.szederz.banking.local.Versioned;

public class LocalAccount implements Account, Versioned {
  private AccountId accountId;
  private Currency balance;
  private long version;

  public LocalAccount(AccountId accountId, Currency balance) {
    this.accountId = accountId;
    this.balance = balance;
    this.version = 0L;
  }

  private LocalAccount(LocalAccount account) {
    this.accountId = account.getAccountId();
    this.balance = account.getBalance();
    this.version = account.getVersion();
  }

  public AccountId getAccountId() {
    return accountId;
  }

  @Override
  public LocalAccount withdraw(Currency amount) {
    LocalAccount account = new LocalAccount(this);
    account.balance = account.balance.minus(amount);
    return account;
  }

  @Override
  public LocalAccount deposit(Currency amount) {
    LocalAccount account = new LocalAccount(this);
    account.balance = account.balance.plus(amount);
    return account;
  }

  @Override
  public Currency getBalance() {
    return balance;
  }

  @Override
  public long getVersion() {
    return version;
  }

  @Override
  public LocalAccount withIncreasedVersion() {
    LocalAccount account = new LocalAccount(this);
    account.version += 1L;
    return account;
  }
}
