package org.szederz.banking.interactor.open;

import org.szederz.banking.Account;
import org.szederz.banking.AccountId;
import org.szederz.banking.Bank;

import java.util.function.Function;

import static org.szederz.banking.interactor.ResponseCode.DUPLICATE_TRANSACTION;
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
    AccountId accountId = request.getAccountId();

    if(bank.get(accountId).isPresent()) {
      return new OpenAccountResponse(DUPLICATE_TRANSACTION);
    }

    Account account = accountBuilder.apply(request);

    bank.put(account);

    return new OpenAccountResponse(TRANSACTION_APPROVED, account);
  }
}
