package org.szederz.banking.interactor.read;

import org.szederz.banking.Account;
import org.szederz.banking.interactor.Response;
import org.szederz.banking.interactor.ResponseCode;

import java.util.Optional;

public class ReadAccountResponse extends Response {
  private Optional<Account> optionalAccount = Optional.empty();

  public ReadAccountResponse(ResponseCode code) {
    super(code);
  }

  public ReadAccountResponse(ResponseCode code, Account account) {
    super(code);
    optionalAccount = Optional.of(account);
  }

  public Optional<Account> getAccount() {
    return optionalAccount;
  }
}
