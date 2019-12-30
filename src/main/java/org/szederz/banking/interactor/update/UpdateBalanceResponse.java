package org.szederz.banking.interactor.update;

import org.szederz.banking.Account;
import org.szederz.banking.interactor.Response;
import org.szederz.banking.interactor.ResponseCode;

import java.util.Optional;

public class UpdateBalanceResponse extends Response {
  private Optional<Account> account = Optional.empty();

  public UpdateBalanceResponse(ResponseCode code) {
    super(code);
  }

  public UpdateBalanceResponse withAccount(Account account) {
    this.account = Optional.of(account);
    return this;
  }

  public Optional<Account> getAccount() {
    return account;
  }
}
