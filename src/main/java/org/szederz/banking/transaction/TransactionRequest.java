package org.szederz.banking.transaction;

import org.szederz.banking.AccountIdentifier;
import org.szederz.banking.currency.Currency;

public class TransactionRequest {
  private Currency amount;
  private AccountIdentifier donorAccountId;
  private AccountIdentifier recipientAccountNumber;

  public TransactionRequest ofAmount(Currency amount) {
    this.amount = amount;
    return this;
  }

  public TransactionRequest fromAccount(AccountIdentifier donorAccountId) {
    this.donorAccountId = donorAccountId;
    return this;
  }

  public TransactionRequest toAccount(AccountIdentifier recipientAccountId) {
    this.recipientAccountNumber = recipientAccountId;
    return this;
  }

  public Currency getAmount() {
    return this.amount;
  }

  public AccountIdentifier getDonorAccountId() {
    return this.donorAccountId;
  }

  public AccountIdentifier getRecipientAccountId() {
    return this.recipientAccountNumber;
  }
}
