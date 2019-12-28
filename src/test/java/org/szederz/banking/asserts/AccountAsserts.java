package org.szederz.banking.asserts;

import org.szederz.banking.Account;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountAsserts {
  public static void assertBalanceOfAccount(int amount, Account account) {
    assertEquals(amount, account.getBalance().getAmount());
  }
}
