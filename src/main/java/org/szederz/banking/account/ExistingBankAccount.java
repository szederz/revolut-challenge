package org.szederz.banking.account;

import org.szederz.banking.currency.Currency;

public class ExistingBankAccount implements BankAccount {
  private AccountNumber accountNumber;
  private Currency balance;

  public ExistingBankAccount(AccountNumber accountNumber) {
    this(accountNumber, Currency.ZERO);
  }

  public ExistingBankAccount(AccountNumber accountNumber, Currency balance) {
    this.accountNumber = accountNumber;
    this.balance = balance;
  }

  public AccountNumber getAccountNumber() {
    return accountNumber;
  }

  @Override
  public BankAccount withdraw(Currency amount) {
    return new ExistingBankAccount(accountNumber, balance.minus(amount));
  }

  @Override
  public BankAccount deposit(Currency amount) {
    return new ExistingBankAccount(accountNumber, balance.plus(amount));
  }

  public ExistingBankAccount withBalance(Currency balance) {
    this.balance = balance;

    return this;
  }

  @Override
  public Currency getBalance() {
    return balance;
  }

  @Override
  public boolean hasAtLeast(Currency amount) {
    return balance.getAmount() >= amount.getAmount();
  }
}
