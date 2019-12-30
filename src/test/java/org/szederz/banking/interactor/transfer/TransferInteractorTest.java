package org.szederz.banking.interactor.transfer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.szederz.banking.Account;
import org.szederz.banking.AccountId;
import org.szederz.banking.components.local.LocalBank;
import org.szederz.banking.components.local.account.currency.LocalCurrency;
import org.szederz.banking.interactor.ResponseCode;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.szederz.banking.asserts.AccountAsserts.assertBalanceOfAccount;
import static org.szederz.banking.interactor.ResponseCode.*;
import static org.szederz.banking.interactor.transfer.TransferTransactionTestHelper.DONOR_ACCOUNT_NUMBER;
import static org.szederz.banking.interactor.transfer.TransferTransactionTestHelper.RECIPIENT_ACCOUNT_NUMBER;

class TransferInteractorTest {
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

    TransferResponse response = helper.transfer();

    assertEquals(INSUFFICIENT_FUNDS, response.getCode());
    assertBalanceOfAccountWithId(0, DONOR_ACCOUNT_NUMBER);
    assertBalanceOfAccountWithId(0, RECIPIENT_ACCOUNT_NUMBER);
  }

  @Test
  void shouldNotTransferMoneyWithInsufficientFunds() {
    helper.registerAccount(DONOR_ACCOUNT_NUMBER, 10);
    helper.registerAccount(RECIPIENT_ACCOUNT_NUMBER, 20);
    helper.request.ofAmount(new LocalCurrency(20));

    TransferResponse response = helper.transfer();

    assertEquals(INSUFFICIENT_FUNDS, response.getCode());
    assertBalanceOfAccountWithId(10, DONOR_ACCOUNT_NUMBER);
    assertBalanceOfAccountWithId(20, RECIPIENT_ACCOUNT_NUMBER);
  }

  @Test
  void respondWithTransactionApprovedWhenAmountCanBeWithdrawn() {
    helper.registerAccount(DONOR_ACCOUNT_NUMBER, 1);
    helper.registerAccount(RECIPIENT_ACCOUNT_NUMBER, 0);

    TransferResponse response = helper.transfer();

    assertEquals(TRANSACTION_APPROVED, response.getCode());

    assertBalanceOfAccountWithId(0, DONOR_ACCOUNT_NUMBER);
    assertBalanceOfAccountWithId(1, RECIPIENT_ACCOUNT_NUMBER);
  }

  @Test
  void worksWithDifferentBalance() {
    helper.registerAccount(DONOR_ACCOUNT_NUMBER, 30);
    helper.registerAccount(RECIPIENT_ACCOUNT_NUMBER, 50);
    helper.request.ofAmount(new LocalCurrency(25));

    TransferResponse response = helper.transfer();

    assertEquals(TRANSACTION_APPROVED, response.getCode());

    assertBalanceOfAccountWithId(5, DONOR_ACCOUNT_NUMBER);
    assertBalanceOfAccountWithId(75, RECIPIENT_ACCOUNT_NUMBER);
  }

  @Test
  void shouldReturnWithBankResponse() {
    helper.bank = new LocalBank() {
      @Override
      public ResponseCode updateAll(List<Account> accounts) {
        super.updateAll(accounts);
        return REENTER_LAST_TRANSACTION;
      }
    };
    helper.interactor = new TransferInteractor(helper.bank);

    helper.registerAccount(DONOR_ACCOUNT_NUMBER, 1);
    helper.registerAccount(RECIPIENT_ACCOUNT_NUMBER, 1);

    TransferResponse response = helper.transfer();

    assertEquals(REENTER_LAST_TRANSACTION, response.getCode());
  }

  private void assertBalanceOfAccountWithId(int amount, AccountId accountId) {
    assertBalanceOfAccount(amount, helper.getAccount(accountId));
  }
}