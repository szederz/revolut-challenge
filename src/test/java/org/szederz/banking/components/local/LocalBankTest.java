package org.szederz.banking.components.local;

import org.junit.jupiter.api.Test;
import org.szederz.banking.Account;
import org.szederz.banking.components.local.account.LocalAccount;
import org.szederz.banking.components.local.account.currency.LocalCurrency;
import org.szederz.banking.interactor.ResponseCode;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.szederz.banking.asserts.AccountAsserts.assertBalanceOfAccount;
import static org.szederz.banking.components.local.BankTestHelper.ACCOUNT_NUMBER_1;
import static org.szederz.banking.components.local.BankTestHelper.ACCOUNT_NUMBER_2;
import static org.szederz.banking.interactor.ResponseCode.REENTER_LAST_TRANSACTION;

class LocalBankTest {
  private BankTestHelper helper = new BankTestHelper();

  @Test
  void shouldNotReturnWithNotRegisteredAccount() {
    assertFalse(helper.hasAccount(ACCOUNT_NUMBER_1));
  }

  @Test
  void shouldRespondWithRegisteredAccount() {
    helper.registerAccount(ACCOUNT_NUMBER_1, 10);

    Account account = helper.getAccount(ACCOUNT_NUMBER_1);
    assertEquals(ACCOUNT_NUMBER_1, account.getAccountId());
    assertBalanceOfAccount(10, account);
  }

  @Test
  void shouldNotReturnAccountForOtherId() {
    helper.registerAccount(ACCOUNT_NUMBER_1, 10);

    assertFalse(helper.hasAccount(ACCOUNT_NUMBER_2));
  }

  @Test
  void shouldBeAbleToRegisterOtherAccount() {
    helper.registerAccount(ACCOUNT_NUMBER_2, 0);

    Account account = helper.getAccount(ACCOUNT_NUMBER_2);
    assertEquals(ACCOUNT_NUMBER_2, account.getAccountId());
    assertBalanceOfAccount(0, account);
  }

  @Test
  void shouldHandleMultipleAccounts() {
    helper.registerAccount(ACCOUNT_NUMBER_1, 100);
    helper.registerAccount(ACCOUNT_NUMBER_2, 200);

    Account account1 = helper.getAccount(ACCOUNT_NUMBER_1);
    assertEquals(ACCOUNT_NUMBER_1, account1.getAccountId());
    assertBalanceOfAccount(100, account1);

    Account account2 = helper.getAccount(ACCOUNT_NUMBER_2);
    assertEquals(ACCOUNT_NUMBER_2, account2.getAccountId());
    assertBalanceOfAccount(200, account2);
  }

  @Test
  void shouldNotSaveStaleObject() {
    helper.registerAccount(ACCOUNT_NUMBER_1, 100);
    LocalAccount account = helper.getAccount(ACCOUNT_NUMBER_1);
    LocalAccount staleAccount = helper.getAccount(ACCOUNT_NUMBER_1);
    helper.bank.update(
      account.deposit(new LocalCurrency(1)));

    ResponseCode response = helper.bank.update(
      staleAccount.deposit(new LocalCurrency(20)));

    assertEquals(REENTER_LAST_TRANSACTION, response);
  }

  @Test
  void shouldNotSaveAnyWhenFirstIsStale() {
    helper.registerAccount(ACCOUNT_NUMBER_1, 100);
    helper.registerAccount(ACCOUNT_NUMBER_2, 200);

    LocalAccount account1 = helper.getAccount(ACCOUNT_NUMBER_1);
    LocalAccount account1Stale = helper.getAccount(ACCOUNT_NUMBER_1);
    LocalAccount account2 = helper.getAccount(ACCOUNT_NUMBER_2);
    helper.bank.update(
      account1.deposit(new LocalCurrency(1)));

    ResponseCode response = helper.bank.updateAll(asList(
      account1Stale.deposit(new LocalCurrency(10)),
      account2.deposit(new LocalCurrency(10))));

    assertEquals(REENTER_LAST_TRANSACTION, response);

    assertBalanceOfAccount(101, helper.getAccount(ACCOUNT_NUMBER_1));
    assertBalanceOfAccount(200, helper.getAccount(ACCOUNT_NUMBER_2));
  }

  @Test
  void shouldNotSaveAnyWhenLastIsStale() {
    helper.registerAccount(ACCOUNT_NUMBER_1, 100);
    helper.registerAccount(ACCOUNT_NUMBER_2, 200);

    LocalAccount account1 = helper.getAccount(ACCOUNT_NUMBER_1);
    LocalAccount account2 = helper.getAccount(ACCOUNT_NUMBER_2);
    LocalAccount account2Stale = helper.getAccount(ACCOUNT_NUMBER_2);
    helper.bank.update(
      account2.deposit(new LocalCurrency(1)));

    ResponseCode response = helper.bank.updateAll(asList(
      account1.deposit(new LocalCurrency(10)),
      account2Stale.deposit(new LocalCurrency(10))));

    assertEquals(REENTER_LAST_TRANSACTION, response);
    assertBalanceOfAccount(100, helper.getAccount(ACCOUNT_NUMBER_1));
    assertBalanceOfAccount(201, helper.getAccount(ACCOUNT_NUMBER_2));
  }
}