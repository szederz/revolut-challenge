package org.szederz.banking.local;

import org.junit.jupiter.api.Test;
import org.szederz.banking.Account;
import org.szederz.banking.interactor.ResponseCode;
import org.szederz.banking.local.account.LocalAccount;
import org.szederz.banking.local.account.currency.LocalCurrency;
import org.szederz.banking.local.account.identifier.AccountNumber;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.szederz.banking.asserts.AccountAsserts.assertBalanceOfAccount;
import static org.szederz.banking.interactor.ResponseCode.REENTER_LAST_TRANSACTION;
import static org.szederz.banking.local.BankTestHelper.ACCOUNT_NUMBER_1;
import static org.szederz.banking.local.BankTestHelper.ACCOUNT_NUMBER_2;

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
    helper.localBank.put(
      account.deposit(new LocalCurrency(1)));

    ResponseCode response = helper.localBank.put(
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
    helper.localBank.put(
      account1.deposit(new LocalCurrency(1)));

    ResponseCode response = helper.localBank.putAll(asList(
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
    helper.localBank.put(
      account2.deposit(new LocalCurrency(1)));

    ResponseCode response = helper.localBank.putAll(asList(
      account1.deposit(new LocalCurrency(10)),
      account2Stale.deposit(new LocalCurrency(10))));

    assertEquals(REENTER_LAST_TRANSACTION, response);
    assertBalanceOfAccount(100, helper.getAccount(ACCOUNT_NUMBER_1));
    assertBalanceOfAccount(201, helper.getAccount(ACCOUNT_NUMBER_2));
  }

  @Test
  void shouldNotAcceptUpdateFromConcurrentThread() throws InterruptedException {
    AccountNumber accountNumber = new SlowlyHashedAccountNumber(0, 0, 0);
    helper.registerAccount(accountNumber, 100);
    LocalAccount account = helper.getAccount(accountNumber);

    LocalAccount updatedAccount1 = account.deposit(new LocalCurrency(20));
    LocalAccount updatedAccount2 = account.deposit(new LocalCurrency(30));

    Thread thread1 = new Thread(() ->
      helper.localBank.put(updatedAccount1));
    Thread thread2 = new Thread(() ->
      helper.localBank.put(updatedAccount2));

    thread1.start();
    Thread.yield();
    thread2.start();

    thread1.join();
    thread2.join();

    assertBalanceOfAccount(120, helper.getAccount(accountNumber));
  }

  private static class SlowlyHashedAccountNumber extends AccountNumber {
    public SlowlyHashedAccountNumber(int part1, int part2, int part3) {
      super(part1, part2, part3);
    }

    @Override
    public int hashCode() {
      try {
        Thread.sleep(0, 5);
      } catch(InterruptedException e) {
        e.printStackTrace();
      }
      return super.hashCode();
    }
  }
}