package org.szederz.banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.szederz.banking.account.AccountNumber;
import org.szederz.banking.account.ExistingBankAccount;
import org.szederz.banking.currency.LocalCurrency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.szederz.banking.response.AccountResponse.NO_CREDIT_ACCOUNT;

class MissingBankAccountIssueTest {
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
      .toAccountNumber(RECIPIENT_ACCOUNT_NUMBER);
  }

  @Test
  void respondsWithNoAccountIfBankHoldsNone() {
    BankResponse response = bank.transfer(transaction);

    assertEquals(NO_CREDIT_ACCOUNT, response.getCode());
  }

  @Test
  void respondsWithInsufficientFundsWhenWrongAccountIsReferenced() {
    bank = bank.withAccount(new ExistingBankAccount(RECIPIENT_ACCOUNT_NUMBER));

    BankResponse response = bank.transfer(transaction);

    assertEquals(NO_CREDIT_ACCOUNT, response.getCode());
  }

  @Test
  void respondsWithNoCreditAccountIfRecipientIsMissing() {
    bank = bank.withAccount(new ExistingBankAccount(DONOR_ACCOUNT_NUMBER));

    BankResponse response = bank.transfer(transaction);

    assertEquals(NO_CREDIT_ACCOUNT, response.getCode());
  }
}