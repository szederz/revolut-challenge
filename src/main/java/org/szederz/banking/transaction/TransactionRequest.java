package org.szederz.banking.transaction;

import org.szederz.banking.account.identifier.AccountNumber;
import org.szederz.banking.currency.Currency;

public class TransactionRequest {
  private Currency amount;
  private AccountNumber donorAccountNumber;
  private AccountNumber recipientAccountNumber;

  public TransactionRequest ofAmount(Currency amount) {
    this.amount = amount;
    return this;
  }

  public TransactionRequest fromAccountNumber(AccountNumber donorAccountNumber) {
    this.donorAccountNumber = donorAccountNumber;
    return this;
  }

  public AccountNumber getDonorAccountNumber() {
    return this.donorAccountNumber;
  }

  public AccountNumber getRecipientAccountNumber() {
    return this.recipientAccountNumber;
  }

  public TransactionRequest toAccountNumber(AccountNumber recipientAccountNumber) {
    this.recipientAccountNumber = recipientAccountNumber;
    return this;
  }

  public Currency getAmount() {
    return this.amount;
  }
}
