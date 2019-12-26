package org.szederz.banking.interactor.open;

import org.szederz.banking.AccountId;
import org.szederz.banking.Currency;

import java.util.Optional;

public class OpenAccountRequest {
  private Currency balance;
  private AccountId accountId;

  public OpenAccountRequest ofAmount(Currency balance) {
    this.balance = balance;
    return this;
  }

  public OpenAccountRequest withAccountId(AccountId accountId) {
    this.accountId = accountId;
    return this;
  }

  public Currency getBalance() {
    return this.balance;
  }

  public AccountId getAccountId() {
    return accountId;
  }
}
