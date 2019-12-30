package org.szederz.banking.interactor.update;

import org.szederz.banking.AccountId;
import org.szederz.banking.Currency;
import org.szederz.banking.components.local.account.currency.LocalCurrency;

public class UpdateBalanceRequest {
  private AccountId accountId;
  private LocalCurrency withdraw = new LocalCurrency(0);
  private LocalCurrency deposit = new LocalCurrency(0);

  public UpdateBalanceRequest withdraw(LocalCurrency currency) {
    this.withdraw = currency;
    return this;
  }

  public UpdateBalanceRequest withAccountId(AccountId id) {
    this.accountId = id;
    return this;
  }

  public UpdateBalanceRequest deposit(LocalCurrency deposit) {
    this.deposit = deposit;
    return this;
  }

  public AccountId getAccountId() {
    return this.accountId;
  }

  public Currency getWithdraw() {
    return withdraw;
  }

  public Currency getDeposit() {
    return this.deposit;
  }
}
