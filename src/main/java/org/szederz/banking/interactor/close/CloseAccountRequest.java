package org.szederz.banking.interactor.close;

import org.szederz.banking.Account;

public class CloseAccountRequest {
  private Account account;

  public CloseAccountRequest withAccount(Account account) {
    this.account = account;
    return this;
  }

  public Account getAccount() {
    return this.account;
  }
}
