package org.szederz.banking.components.local;

import org.szederz.banking.AccountId;
import org.szederz.banking.components.local.account.LocalAccount;
import org.szederz.banking.components.local.account.currency.LocalCurrency;
import org.szederz.banking.components.local.account.identifier.AccountNumber;

public class BankTestHelper {
  public static final AccountId ACCOUNT_NUMBER_1 =
    new AccountNumber(12345678, 12345678);
  public static final AccountId ACCOUNT_NUMBER_2 =
    new AccountNumber(87654321, 87654321);

  public LocalBank bank = new LocalBank();

  public void registerAccount(AccountId accountId) {
    this.registerAccount(accountId, 0);
  }

  public void registerAccount(AccountId accountId, int balance) {
    bank.create(new LocalAccount(accountId, new LocalCurrency(balance)));
  }

  public LocalAccount getAccount(AccountId accountId) {
    return bank.getLocal(accountId)
      .orElseThrow(() -> new AssertionError(
        String.format("Could not find account with id %s", accountId)));
  }

  public boolean hasAccount(AccountId accountId) {
    return bank.get(accountId).isPresent();
  }
}
