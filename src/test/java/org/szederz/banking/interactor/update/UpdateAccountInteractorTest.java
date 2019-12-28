package org.szederz.banking.interactor.update;

import org.junit.jupiter.api.Test;
import org.szederz.banking.local.account.LocalAccount;
import org.szederz.banking.local.account.currency.LocalCurrency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.szederz.banking.asserts.AccountAsserts.assertBalanceOfAccount;
import static org.szederz.banking.interactor.ResponseCode.*;
import static org.szederz.banking.local.BankTestHelper.ACCOUNT_NUMBER_1;

class UpdateAccountInteractorTest {

  UpdateAccountInteractorTestHelper helper =
    new UpdateAccountInteractorTestHelper();

  @Test
  void shouldNotUpdateMissingAccount() {
    helper.request.withAccount(
      new LocalAccount(ACCOUNT_NUMBER_1)
        .withVersion(0));

    UpdateAccountResponse response = helper.updateAccount();

    assertEquals(NO_CREDIT_ACCOUNT, response.getCode());
  }

  @Test
  void shouldUpdatePresentAccount() {
    helper.request.withAccount(
      new LocalAccount(ACCOUNT_NUMBER_1, new LocalCurrency(10))
        .withVersion(0));
    helper.registerAccount(ACCOUNT_NUMBER_1);

    UpdateAccountResponse response = helper.updateAccount();

    assertEquals(TRANSACTION_APPROVED, response.getCode());
    assertBalanceOfAccount(10, helper.getAccount(ACCOUNT_NUMBER_1));
  }

  @Test
  void shouldNotUpdateStaleAccount() {
    helper.request.withAccount(
      new LocalAccount(ACCOUNT_NUMBER_1, new LocalCurrency(10))
        .withVersion(-1));
    helper.registerAccount(ACCOUNT_NUMBER_1, 5);

    UpdateAccountResponse response = helper.updateAccount();

    assertEquals(REENTER_LAST_TRANSACTION, response.getCode());
    assertBalanceOfAccount(5, helper.getAccount(ACCOUNT_NUMBER_1));
  }
}