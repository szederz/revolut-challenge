package org.szederz.banking.transaction.transfer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.szederz.banking.Account;
import org.szederz.banking.AccountIdentifier;
import org.szederz.banking.currency.LocalCurrency;
import org.szederz.banking.transaction.TransactionResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.szederz.banking.asserts.AccountAsserts.assertBalanceOfAccount;
import static org.szederz.banking.transaction.TransactionCode.INSUFFICIENT_FUNDS;
import static org.szederz.banking.transaction.TransactionCode.TRANSACTION_APPROVED;
import static org.szederz.banking.transaction.transfer.TransferTransactionTestHelper.DONOR_ACCOUNT_NUMBER;
import static org.szederz.banking.transaction.transfer.TransferTransactionTestHelper.RECIPIENT_ACCOUNT_NUMBER;

class TransferTransactionTest {
  private TransferTransactionTestHelper helper = new TransferTransactionTestHelper();

  @BeforeEach
  void setUp() {
    helper.request
      .fromAccount(DONOR_ACCOUNT_NUMBER)
      .toAccount(RECIPIENT_ACCOUNT_NUMBER)
      .ofAmount(new LocalCurrency(1));
  }

  @Test
  void respondsWithInsufficientFundsForEmptyAccount() {
    helper.registerAccount(DONOR_ACCOUNT_NUMBER, 0);
    helper.registerAccount(RECIPIENT_ACCOUNT_NUMBER, 0);

    TransactionResponse response = helper.transfer();

    assertEquals(INSUFFICIENT_FUNDS, response.getCode());
    assertBalanceOfAccountWithId(0, DONOR_ACCOUNT_NUMBER);
    assertBalanceOfAccountWithId(0, RECIPIENT_ACCOUNT_NUMBER);
  }

  @Test
  void shouldNotTransferMoneyWithInsufficientFunds() {
    helper.registerAccount(DONOR_ACCOUNT_NUMBER, 10);
    helper.registerAccount(RECIPIENT_ACCOUNT_NUMBER, 20);
    helper.request.ofAmount(new LocalCurrency(20));

    TransactionResponse response = helper.transfer();

    assertEquals(INSUFFICIENT_FUNDS, response.getCode());
    assertBalanceOfAccountWithId(10, DONOR_ACCOUNT_NUMBER);
    assertBalanceOfAccountWithId(20, RECIPIENT_ACCOUNT_NUMBER);
  }

  @Test
  void respondWithTransactionApprovedWhenAmountCanBeWithdrawn() {
    helper.registerAccount(DONOR_ACCOUNT_NUMBER, 1);
    helper.registerAccount(RECIPIENT_ACCOUNT_NUMBER, 0);

    TransactionResponse response = helper.transfer();

    assertEquals(TRANSACTION_APPROVED, response.getCode());

    assertBalanceOfAccountWithId(0, DONOR_ACCOUNT_NUMBER);
    assertBalanceOfAccountWithId(1, RECIPIENT_ACCOUNT_NUMBER);
  }

  @Test
  void worksWithDifferentBalance() {
    helper.registerAccount(DONOR_ACCOUNT_NUMBER, 30);
    helper.registerAccount(RECIPIENT_ACCOUNT_NUMBER, 50);
    helper.request.ofAmount(new LocalCurrency(25));

    TransactionResponse response = helper.transfer();

    assertEquals(TRANSACTION_APPROVED, response.getCode());

    assertBalanceOfAccountWithId(5, DONOR_ACCOUNT_NUMBER);
    assertBalanceOfAccountWithId(75, RECIPIENT_ACCOUNT_NUMBER);
  }

  private void assertBalanceOfAccountWithId(int amount, AccountIdentifier accountId) {
    assertBalanceOfAccount(amount, helper.getAccount(accountId));
  }

}