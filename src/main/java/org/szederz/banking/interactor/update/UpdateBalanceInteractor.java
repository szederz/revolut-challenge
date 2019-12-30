package org.szederz.banking.interactor.update;

import org.szederz.banking.Account;
import org.szederz.banking.Bank;
import org.szederz.banking.Currency;

import java.util.Optional;

import static org.szederz.banking.interactor.ResponseCode.*;

public class UpdateBalanceInteractor {
  private final Bank bank;

  public UpdateBalanceInteractor(Bank bank) {
    this.bank = bank;
  }

  public UpdateBalanceResponse updateBalance(UpdateBalanceRequest request) {
    Optional<Account> optional = bank.get(request.getAccountId());

    if(!optional.isPresent()) {
      return new UpdateBalanceResponse(NO_CREDIT_ACCOUNT);
    }

    return withdrawFromAccount(optional.get(), request);
  }

  private UpdateBalanceResponse withdrawFromAccount(Account account, UpdateBalanceRequest request) {
    Currency difference = request.getWithdraw().minus(request.getDeposit());

    if(account.getBalance().isLessThan(difference)) {
      return new UpdateBalanceResponse(INSUFFICIENT_FUNDS);
    }

    Account withdrawnAccount = account.withdraw(difference);
    UpdateBalanceResponse response = new UpdateBalanceResponse(bank.update(withdrawnAccount));

    if(response.getCode() == TRANSACTION_APPROVED) {
      Optional<Account> updatedAccount = bank.get(account.getAccountId());
      if(updatedAccount.isPresent()) {
        return response.withAccount(updatedAccount.get());
      }
    }

    return response;
  }
}
