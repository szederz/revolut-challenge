package org.szederz.banking.interactor.update;

import org.szederz.banking.Account;
import org.szederz.banking.Bank;
import org.szederz.banking.Currency;
import org.szederz.banking.interactor.ResponseCode;

import static org.szederz.banking.interactor.ResponseCode.INSUFFICIENT_FUNDS;
import static org.szederz.banking.interactor.ResponseCode.NO_CREDIT_ACCOUNT;

public class UpdateBalanceInteractor {
  private final Bank bank;

  public UpdateBalanceInteractor(Bank bank) {
    this.bank = bank;
  }

  public UpdateBalanceResponse updateBalance(UpdateBalanceRequest request) {
    return new UpdateBalanceResponse(bank.get(request.getAccountId())
      .map(account -> updateBalanceOfAccount(account, request))
      .orElse(NO_CREDIT_ACCOUNT)
    );
  }

  private ResponseCode updateBalanceOfAccount(Account account, UpdateBalanceRequest request) {
    Currency difference = request.getWithdraw()
      .minus(request.getDeposit());

    if(account.getBalance().isLessThan(difference)) {
      return INSUFFICIENT_FUNDS;
    }

    return bank.update(account.withdraw(difference));
  }
}
