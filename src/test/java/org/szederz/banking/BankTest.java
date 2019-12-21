package org.szederz.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.szederz.banking.account.AccountNumber;
import org.szederz.banking.account.BankAccount;
import org.szederz.banking.account.ExistingBankAccount;
import org.szederz.banking.currency.LocalCurrency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.szederz.banking.response.AccountResponse.INSUFFICIENT_FUNDS;
import static org.szederz.banking.response.AccountResponse.TRANSACTION_APPROVED;

class BankTest {
  private static final AccountNumber DONOR_ACCOUNT_NUMBER =
    new AccountNumber(12345678, 12345678);
  private static final AccountNumber RECIPIENT_ACCOUNT_NUMBER =
    new AccountNumber(87654321, 87654321);

  private Bank bank;
  private Transaction transaction;

  @BeforeEach
  void setUp() {
    bank = new Bank();

    transaction = new Transaction()
      .fromAccountNumber(DONOR_ACCOUNT_NUMBER)
      .toAccountNumber(RECIPIENT_ACCOUNT_NUMBER)
      .ofAmount(new LocalCurrency(1));
  }

  @Test
  void respondsWithInsufficientFundsForEmptyAccount() {
    bank = bank
      .withAccount(new ExistingBankAccount(DONOR_ACCOUNT_NUMBER))
      .withAccount(new ExistingBankAccount(RECIPIENT_ACCOUNT_NUMBER));

    BankResponse response = bank.transfer(transaction);

    assertEquals(INSUFFICIENT_FUNDS, response.getCode());
  }

  @Test
  void respondWithInsufficientFundsWhenAccountIsEmptyInMultipleAccounts() {
    bank = bank
      .withAccount(new ExistingBankAccount(DONOR_ACCOUNT_NUMBER))
      .withAccount(new ExistingBankAccount(RECIPIENT_ACCOUNT_NUMBER));

    BankResponse response = bank.transfer(transaction);

    assertEquals(INSUFFICIENT_FUNDS, response.getCode());
  }

  @Test
  void respondWithTransactionApprovedWhenAmountCanBeWithdrawn() {
    bank = bank
      .withAccount(new ExistingBankAccount(DONOR_ACCOUNT_NUMBER)
        .withBalance(new LocalCurrency(1)))
      .withAccount(new ExistingBankAccount(RECIPIENT_ACCOUNT_NUMBER)
        .withBalance(new LocalCurrency(0)));

    BankResponse response = bank.transfer(transaction);

    assertEquals(TRANSACTION_APPROVED, response.getCode());

    assertEquals(0, getAccount(DONOR_ACCOUNT_NUMBER).getBalance().getAmount());
    assertEquals(1, getAccount(RECIPIENT_ACCOUNT_NUMBER).getBalance().getAmount());
  }

  private BankAccount getAccount(AccountNumber accountNumber) {
    return bank.getAccount(accountNumber)
      .orElseThrow(AssertionError::new);
  }
}