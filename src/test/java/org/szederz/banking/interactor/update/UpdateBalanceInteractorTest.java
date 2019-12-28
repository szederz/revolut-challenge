package org.szederz.banking.interactor.update;

import org.junit.jupiter.api.Test;
import org.szederz.banking.local.account.currency.LocalCurrency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.szederz.banking.asserts.AccountAsserts.assertBalanceOfAccount;
import static org.szederz.banking.interactor.ResponseCode.*;
import static org.szederz.banking.local.BankTestHelper.ACCOUNT_NUMBER_1;
import static org.szederz.banking.local.BankTestHelper.ACCOUNT_NUMBER_2;

class UpdateBalanceInteractorTest {

  UpdateBalanceInteractorTestHelper helper =
    new UpdateBalanceInteractorTestHelper();

  @Test
  void shouldNotUpdateMissingAccount() {
    helper.request
      .withAccountId(ACCOUNT_NUMBER_1)
      .withdraw(new LocalCurrency(10));

    UpdateBalanceResponse response = helper.updateAccount();

    assertEquals(NO_CREDIT_ACCOUNT, response.getCode());
  }

  @Test
  void shouldWithdrawFromExistingAccount() {
    helper.request
      .withAccountId(ACCOUNT_NUMBER_1)
      .withdraw(new LocalCurrency(10));
    helper.registerAccount(ACCOUNT_NUMBER_1, 20);

    UpdateBalanceResponse response = helper.updateAccount();

    assertEquals(TRANSACTION_APPROVED, response.getCode());
    assertBalanceOfAccount(10, helper.getAccount(ACCOUNT_NUMBER_1));
  }

  @Test
  void shouldNotWithdrawFromInsufficientFunds() {
    helper.request
      .withAccountId(ACCOUNT_NUMBER_1)
      .withdraw(new LocalCurrency(10));
    helper.registerAccount(ACCOUNT_NUMBER_1, 5);

    UpdateBalanceResponse response = helper.updateAccount();

    assertEquals(INSUFFICIENT_FUNDS, response.getCode());
    assertBalanceOfAccount(5, helper.getAccount(ACCOUNT_NUMBER_1));
  }

  @Test
  void shouldWithdrawWhenDepositIsHigherThanWithDraw() {
    helper.request
      .withAccountId(ACCOUNT_NUMBER_1)
      .deposit(new LocalCurrency(11))
      .withdraw(new LocalCurrency(10));
    helper.registerAccount(ACCOUNT_NUMBER_1, 5);

    UpdateBalanceResponse response = helper.updateAccount();

    assertEquals(TRANSACTION_APPROVED, response.getCode());
    assertBalanceOfAccount(6, helper.getAccount(ACCOUNT_NUMBER_1));
  }

  @Test
  void shouldDepositToExistingAccount() {
    helper.request
      .withAccountId(ACCOUNT_NUMBER_2)
      .deposit(new LocalCurrency(10));
    helper.registerAccount(ACCOUNT_NUMBER_2, 20);

    UpdateBalanceResponse response = helper.updateAccount();

    assertEquals(TRANSACTION_APPROVED, response.getCode());
    assertBalanceOfAccount(30, helper.getAccount(ACCOUNT_NUMBER_2));
  }
}