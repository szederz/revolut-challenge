package org.szederz.banking;

import org.junit.jupiter.api.Test;
import org.szederz.banking.account.LocalAccount;
import org.szederz.banking.currency.LocalCurrency;
import org.szederz.banking.transaction.TransactionCode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.szederz.banking.BankTestHelper.ACCOUNT_NUMBER_1;
import static org.szederz.banking.BankTestHelper.ACCOUNT_NUMBER_2;
import static org.szederz.banking.asserts.AccountAsserts.assertBalanceOfAccount;
import static org.szederz.banking.transaction.TransactionCode.REENTER_LAST_TRANSACTION;

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
  void shouldNotSaveStaleObjects() {
    helper.registerAccount(ACCOUNT_NUMBER_1, 100);
    LocalAccount account = helper.getAccount(ACCOUNT_NUMBER_1);
    LocalAccount staleAccount = helper.getAccount(ACCOUNT_NUMBER_1);
    helper.localBank.saveAccount(
      account.deposit(new LocalCurrency(1)));

    TransactionCode response = helper.localBank.saveAccount(
      staleAccount.deposit(new LocalCurrency(20)));

    assertEquals(REENTER_LAST_TRANSACTION, response);
  }
}