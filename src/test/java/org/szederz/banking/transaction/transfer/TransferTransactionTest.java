package org.szederz.banking.transaction.transfer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.szederz.banking.Account;
import org.szederz.banking.account.identifier.AccountNumber;
import org.szederz.banking.account.LocalAccount;
import org.szederz.banking.currency.LocalCurrency;
import org.szederz.banking.transaction.TransactionResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.szederz.banking.transaction.TransactionCode.INSUFFICIENT_FUNDS;
import static org.szederz.banking.transaction.TransactionCode.TRANSACTION_APPROVED;
import static org.szederz.banking.transaction.transfer.MissingAccountTest.DONOR_ACCOUNT_NUMBER;
import static org.szederz.banking.transaction.transfer.MissingAccountTest.RECIPIENT_ACCOUNT_NUMBER;

class TransferTransactionTest {
  private TransferTransactionTestHelper helper = new TransferTransactionTestHelper();

  @BeforeEach
  void setUp() {
    helper.request
      .fromAccountNumber(DONOR_ACCOUNT_NUMBER)
      .toAccountNumber(RECIPIENT_ACCOUNT_NUMBER)
      .ofAmount(new LocalCurrency(1));
  }

  @Test
  void respondsWithInsufficientFundsForEmptyAccount() {
    helper.bank.saveAccount(new LocalAccount(DONOR_ACCOUNT_NUMBER));
    helper.bank.saveAccount(new LocalAccount(RECIPIENT_ACCOUNT_NUMBER));

    TransactionResponse response = helper.transfer();

    assertEquals(INSUFFICIENT_FUNDS, response.getCode());
  }

  @Test
  void respondWithInsufficientFundsWhenAccountIsEmptyInMultipleAccounts() {
    helper.bank.saveAccount(new LocalAccount(DONOR_ACCOUNT_NUMBER));
    helper.bank.saveAccount(new LocalAccount(RECIPIENT_ACCOUNT_NUMBER));

    TransactionResponse response = helper.transfer();

    assertEquals(INSUFFICIENT_FUNDS, response.getCode());
  }

  @Test
  void respondWithTransactionApprovedWhenAmountCanBeWithdrawn() {
    helper.bank.saveAccount(new LocalAccount(DONOR_ACCOUNT_NUMBER)
        .withBalance(new LocalCurrency(1)));
    helper.bank.saveAccount(new LocalAccount(RECIPIENT_ACCOUNT_NUMBER)
        .withBalance(new LocalCurrency(0)));

    TransactionResponse response = helper.transfer();

    assertEquals(TRANSACTION_APPROVED, response.getCode());

    assertEquals(0, getAccount(DONOR_ACCOUNT_NUMBER).getBalance().getAmount());
    assertEquals(1, getAccount(RECIPIENT_ACCOUNT_NUMBER).getBalance().getAmount());
  }

  private Account getAccount(AccountNumber accountNumber) {
    return helper.bank.getAccount(accountNumber)
      .orElseThrow(AssertionError::new);
  }
}