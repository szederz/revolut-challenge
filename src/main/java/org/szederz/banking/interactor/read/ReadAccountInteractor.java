package org.szederz.banking.interactor.read;

import org.szederz.banking.local.LocalBank;

import static org.szederz.banking.interactor.ResponseCode.NO_CREDIT_ACCOUNT;
import static org.szederz.banking.interactor.ResponseCode.TRANSACTION_APPROVED;

public class ReadAccountInteractor {
  private final LocalBank bank;

  public ReadAccountInteractor(LocalBank bank) {
    this.bank = bank;
  }

  public ReadAccountResponse readAccount(ReadAccountRequest request) {
    return bank.get(request.getAccountId())
      .map(account -> new ReadAccountResponse(TRANSACTION_APPROVED, account))
      .orElseGet(() -> new ReadAccountResponse(NO_CREDIT_ACCOUNT));
  }
}
