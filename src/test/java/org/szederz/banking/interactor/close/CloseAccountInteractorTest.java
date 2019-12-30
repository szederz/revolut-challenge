package org.szederz.banking.interactor.close;

import org.junit.jupiter.api.Test;
import org.szederz.banking.components.local.account.LocalAccount;

import static org.junit.jupiter.api.Assertions.*;
import static org.szederz.banking.components.local.BankTestHelper.ACCOUNT_NUMBER_1;
import static org.szederz.banking.interactor.ResponseCode.*;

class CloseAccountInteractorTest {
  private CloseAccountInteractorTestHelper helper =
    new CloseAccountInteractorTestHelper();

  @Test
  void shouldResponseWithNoCreditAccountWhenAccountIsMissing() {
    helper.request.withAccount(
      new LocalAccount(ACCOUNT_NUMBER_1));

    CloseAccountResponse response = helper.closeAccount();

    assertEquals(NO_CREDIT_ACCOUNT, response.getCode());
  }

  @Test
  void shouldBeAbleToDeleteExistingAccount() {
    helper.registerAccount(ACCOUNT_NUMBER_1);
    helper.request.withAccount(
      new LocalAccount(ACCOUNT_NUMBER_1)
        .withVersion(0));

    CloseAccountResponse response = helper.closeAccount();

    assertEquals(TRANSACTION_APPROVED, response.getCode());
    assertFalse(helper.hasAccount(ACCOUNT_NUMBER_1));
  }

  @Test
  void shouldnotBeAbleToRemoveStaleAccounts() {
    helper.registerAccount(ACCOUNT_NUMBER_1);
    helper.request.withAccount(
      new LocalAccount(ACCOUNT_NUMBER_1)
        .withVersion(-1));

    CloseAccountResponse response = helper.closeAccount();

    assertEquals(REENTER_LAST_TRANSACTION, response.getCode());
    assertTrue(helper.hasAccount(ACCOUNT_NUMBER_1));
  }

}