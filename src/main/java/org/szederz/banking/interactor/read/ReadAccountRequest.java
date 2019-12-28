package org.szederz.banking.interactor.read;

import org.szederz.banking.AccountId;

public class ReadAccountRequest {
  private AccountId accountId;

  public ReadAccountRequest withAccountId(AccountId accountId) {
    this.accountId = accountId;
    return this;
  }

  public AccountId getAccountId() {
    return accountId;
  }
}
