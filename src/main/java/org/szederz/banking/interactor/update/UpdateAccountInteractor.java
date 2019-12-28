package org.szederz.banking.interactor.update;

import org.szederz.banking.Account;
import org.szederz.banking.Bank;

import static org.szederz.banking.interactor.ResponseCode.NO_CREDIT_ACCOUNT;

public class UpdateAccountInteractor {
  private final Bank bank;

  public UpdateAccountInteractor(Bank bank) {
    this.bank = bank;
  }

  public UpdateAccountResponse updateAccount(UpdateAccountRequest request) {
    Account account = request.getAccount();

    return new UpdateAccountResponse(bank.get(account.getAccountId())
      .map(savedAccount -> bank.update(account))
      .orElse(NO_CREDIT_ACCOUNT)
    );
  }
}
