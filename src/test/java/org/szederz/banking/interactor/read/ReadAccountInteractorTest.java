package org.szederz.banking.interactor.read;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.szederz.banking.components.local.BankTestHelper.ACCOUNT_NUMBER_1;
import static org.szederz.banking.interactor.ResponseCode.NO_CREDIT_ACCOUNT;
import static org.szederz.banking.interactor.ResponseCode.TRANSACTION_APPROVED;

class ReadAccountInteractorTest {
  ReadAccountInteractorTestHelper helper = new ReadAccountInteractorTestHelper();

  @Test
  void shouldReturnWithNoCreditAccountWhenAccountIsMissing() {
    helper.request.withAccountId(ACCOUNT_NUMBER_1);
    ReadAccountResponse response = helper.readAccount();

    assertEquals(NO_CREDIT_ACCOUNT, response.getCode());
  }

  @Test
  void shouldReturnWithAccountWhenPresent() {
    helper.request.withAccountId(ACCOUNT_NUMBER_1);
    helper.registerAccount(ACCOUNT_NUMBER_1);
    ReadAccountResponse response = helper.readAccount();

    assertEquals(TRANSACTION_APPROVED, response.getCode());
    assertTrue(response.getAccount().isPresent());
  }
}