package org.szederz.banking.interactor.open;

import org.szederz.banking.Account;
import org.szederz.banking.Bank;
import org.szederz.banking.interactor.ResponseCode;

import java.util.function.Function;

import static org.szederz.banking.interactor.ResponseCode.TRANSACTION_APPROVED;

public class OpenAccountInteractor {
  private final Bank bank;
  private final Function<OpenAccountRequest, Account> accountBuilder;

  public OpenAccountInteractor(
    Bank bank,
    Function<OpenAccountRequest, Account> accountBuilder
  ) {
    this.bank = bank;
    this.accountBuilder = accountBuilder;
  }

  public OpenAccountResponse openAccount(OpenAccountRequest request) {
    Account account = accountBuilder.apply(request);
    ResponseCode response = bank.create(account);

    if(response == TRANSACTION_APPROVED) {
      return new OpenAccountResponse(response, account);
    }

    return new OpenAccountResponse(response);
  }
}
