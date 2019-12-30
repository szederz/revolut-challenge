package org.szederz.banking.components.local;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.szederz.banking.Account;
import org.szederz.banking.components.local.account.LocalAccount;
import org.szederz.banking.components.local.account.currency.LocalCurrency;
import org.szederz.banking.components.local.account.identifier.AccountNumber;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.szederz.banking.asserts.AccountAsserts.assertBalanceOfAccount;

@Disabled
class LocalBankConcurrencyTest {
  private BankTestHelper helper = new BankTestHelper();

  @Test
  void shouldNotAcceptUpdateFromConcurrentThread() throws InterruptedException {
    for(int i = 0; i < 10; i++) {
      SlowlyHashedAccountNumber accountNumber = new SlowlyHashedAccountNumber(0, 0, 0);
      helper.registerAccount(accountNumber, 100);
      LocalAccount account = helper.getAccount(accountNumber);

      LocalAccount updatedAccount1 = account.deposit(new LocalCurrency(20));
      LocalAccount updatedAccount2 = account.deposit(new LocalCurrency(30));

      startAll(
        new Thread(() ->
          helper.bank.update(updatedAccount1)),
        new SlowThread(() ->
          helper.bank.update(updatedAccount2)));

      assertBalanceOfAccount(120, helper.getAccount(accountNumber));
      helper.bank.remove(helper.getAccount(accountNumber));
    }
  }

  @Test
  void shouldBeThreadSafeDuringDelete() throws InterruptedException {
    for(int i = 0; i < 10; i++) {
      SlowlyHashedAccountNumber accountNumber = new SlowlyHashedAccountNumber(0, 0, 0);
      helper.registerAccount(accountNumber, 100);
      LocalAccount account = helper.getAccount(accountNumber);

      LocalAccount updatedAccount = account.deposit(new LocalCurrency(20));

      startAll(
        new Thread(() ->
          helper.bank.remove(account)),
        new SlowThread(() ->
          helper.bank.update(updatedAccount)));

      assertFalse(helper.hasAccount(accountNumber));
      helper.bank.remove(account);
    }
  }

  @Test
  void shouldBeThreadSafeDuringRead() throws InterruptedException {
    for(int i = 0; i < 10; i++) {
      SlowlyHashedAccountNumber accountNumber = new SlowlyHashedAccountNumber(0, 0, 0);
      helper.registerAccount(accountNumber, 100);
      LocalAccount account = helper.getAccount(accountNumber);
      Container<Optional<Account>> container = new Container<>();

      startAll(
        new Thread(() ->
          helper.bank.remove(account)),
        new SlowThread(() ->
          container.set(helper.bank.get(accountNumber))));

      assertFalse(helper.hasAccount(accountNumber));
      assertNotNull(container.get());
      helper.bank.remove(account);
    }
  }

  private void startAll(Thread... threads) throws InterruptedException {
    for(Thread thread : threads) {
      thread.start();
    }

    for(Thread thread : threads) {
      thread.join();
    }
  }

  private static class SlowThread extends Thread {
    public SlowThread(Runnable runnable) {
      super(runnable);
    }

    @Override
    public void run() {
      try {
        Thread.sleep(0, 10);
      } catch(InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      super.run();
    }
  }

  private static class Container<T> {
    T t;

    public T get() {
      return t;
    }

    public void set(T t) {
      this.t = t;
    }
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
        Thread.currentThread().interrupt();
      }
      return super.hashCode();
    }
  }
}