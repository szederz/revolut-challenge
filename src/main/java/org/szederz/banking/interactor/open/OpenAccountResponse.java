package org.szederz.banking.interactor.open;

import org.szederz.banking.Account;
import org.szederz.banking.interactor.Response;
import org.szederz.banking.interactor.ResponseCode;

import java.util.Optional;

public class OpenAccountResponse extends Response {
  private final Optional<Account> optionalAccount;

  public OpenAccountResponse(ResponseCode code, Account account) {
    super(code);
    this.optionalAccount = Optional.of(account);
  }

  public OpenAccountResponse(ResponseCode code) {
    super(code);
    this.optionalAccount = Optional.empty();
  }

  public Optional<Account> getAccount() {
    return this.optionalAccount;
  }
}
