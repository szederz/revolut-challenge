package org.szederz.banking.interactor.transfer;

import org.szederz.banking.AccountId;
import org.szederz.banking.Currency;

public class TransferRequest {
  private Currency amount;
  private AccountId donorAccountId;
  private AccountId recipientAccountId;

  public TransferRequest ofAmount(Currency amount) {
    this.amount = amount;
    return this;
  }

  public TransferRequest fromAccount(AccountId donorAccountId) {
    this.donorAccountId = donorAccountId;
    return this;
  }

  public TransferRequest toAccount(AccountId recipientAccountId) {
    this.recipientAccountId = recipientAccountId;
    return this;
  }

  public Currency getAmount() {
    return this.amount;
  }

  public AccountId getDonorAccountId() {
    return this.donorAccountId;
  }

  public AccountId getRecipientAccountId() {
    return this.recipientAccountId;
  }
}
