package org.szederz.banking;

import org.szederz.banking.account.AccountNumber;
import org.szederz.banking.currency.Currency;

public class Transaction {
  private Currency amount;
  private AccountNumber donorAccountNumber;
  private AccountNumber recipientAccountNumber;

  public Transaction ofAmount(Currency amount) {
    this.amount = amount;
    return this;
  }

  public Transaction fromAccountNumber(AccountNumber donorAccountNumber) {
    this.donorAccountNumber = donorAccountNumber;
    return this;
  }

  public AccountNumber getDonorAccountNumber() {
    return this.donorAccountNumber;
  }

  public AccountNumber getRecipientAccountNumber() {
    return this.recipientAccountNumber;
  }

  public Transaction toAccountNumber(AccountNumber recipientAccountNumber) {
    this.recipientAccountNumber = recipientAccountNumber;
    return this;
  }

  public Currency getAmount() {
    return this.amount;
  }
}
