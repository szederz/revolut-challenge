package org.szederz.banking.interactor.update;

import org.szederz.banking.Account;

public class UpdateAccountRequest {
  private Account account;

  public UpdateAccountRequest withAccount(Account account) {
    this.account = account;
    return this;
  }

  public Account getAccount() {
    return account;
  }
}
