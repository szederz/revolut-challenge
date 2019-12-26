package org.szederz.banking.interactor.open;

import org.szederz.banking.Account;
import org.szederz.banking.interactor.ResponseCode;

import java.util.Optional;

public class OpenAccountResponse {
  private final ResponseCode code;
  private final Optional<Account> optionalAccount;

  public OpenAccountResponse(ResponseCode code, Account account) {
    this.code = code;
    this.optionalAccount = Optional.of(account);
  }

  public OpenAccountResponse(ResponseCode code) {
    this.code = code;
    this.optionalAccount = Optional.empty();
  }

  public ResponseCode getCode() {
    return code;
  }

  public Optional<Account> getAccount() {
    return this.optionalAccount;
  }
}
