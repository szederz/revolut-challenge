package org.szederz.banking.interactor.open;

import org.junit.jupiter.api.Test;
import org.szederz.banking.Account;
import org.szederz.banking.AccountId;
import org.szederz.banking.local.account.LocalAccount;
import org.szederz.banking.local.account.currency.LocalCurrency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.szederz.banking.BankTestHelper.ACCOUNT_NUMBER_1;
import static org.szederz.banking.BankTestHelper.ACCOUNT_NUMBER_2;
import static org.szederz.banking.interactor.ResponseCode.DUPLICATE_TRANSACTION;
import static org.szederz.banking.interactor.ResponseCode.TRANSACTION_APPROVED;

class OpenAccountInteractorTest {
  private OpenAccountInteractorTestHelper helper = new OpenAccountInteractorTestHelper();

  @Test
  void shouldOpenAccountWithZeroBalance() {
    helper.request
      .ofAmount(new LocalCurrency(0))
      .withAccountId(ACCOUNT_NUMBER_1);

    OpenAccountResponse response = helper.openAccount();

    assertEquals(TRANSACTION_APPROVED, response.getCode());

    AccountId accountNumber = getAccountFrom(response).getAccountId();
    assertEquals(ACCOUNT_NUMBER_1, accountNumber);
    LocalAccount account = helper.getAccount(accountNumber);
    assertEquals(ACCOUNT_NUMBER_1, account.getAccountId());
    assertEquals(new LocalCurrency(0), account.getBalance());
  }

  @Test
  void shouldOpenAccountWithNonZeroBalance() {
    helper.request
      .ofAmount(new LocalCurrency(10))
      .withAccountId(ACCOUNT_NUMBER_1);

    OpenAccountResponse response = helper.openAccount();

    assertEquals(TRANSACTION_APPROVED, response.getCode());

    AccountId accountNumber = getAccountFrom(response).getAccountId();
    assertEquals(ACCOUNT_NUMBER_1, accountNumber);
    LocalAccount account = helper.getAccount(accountNumber);
    assertEquals(ACCOUNT_NUMBER_1, account.getAccountId());
    assertEquals(new LocalCurrency(10), account.getBalance());
  }

  private Account getAccountFrom(OpenAccountResponse response) {
    return response.getAccount()
      .orElseThrow(() -> new AssertionError("Response does not contain account"));
  }

  @Test
  void shouldOpenAccountWithNonExistingAccountNumber() {
    helper.request
      .ofAmount(new LocalCurrency(10))
      .withAccountId(ACCOUNT_NUMBER_2);

    OpenAccountResponse response = helper.openAccount();

    AccountId accountNumber = getAccountFrom(response).getAccountId();
    assertEquals(TRANSACTION_APPROVED, response.getCode());
    assertEquals(ACCOUNT_NUMBER_2, helper.getAccount(accountNumber).getAccountId());
  }

  @Test
  void shouldRejectAccountIdsThatAreAlreadyPresent() {
    helper.request
      .ofAmount(new LocalCurrency(10))
      .withAccountId(ACCOUNT_NUMBER_1);
    helper.openAccount();

    OpenAccountResponse response = helper.openAccount();

    assertEquals(DUPLICATE_TRANSACTION, response.getCode());
    assertEquals(ACCOUNT_NUMBER_1, helper.getAccount(ACCOUNT_NUMBER_1).getAccountId());
  }
}