package org.szederz.banking.interactor.close;

import org.szederz.banking.Bank;
import org.szederz.banking.interactor.ResponseCode;

public class CloseAccountInteractor {
  private final Bank bank;

  public CloseAccountInteractor(Bank bank) {
    this.bank = bank;
  }

  public CloseAccountResponse closeAccount(CloseAccountRequest request) {
    ResponseCode code = bank.remove(request.getAccount());
    return new CloseAccountResponse(code);
  }
}
