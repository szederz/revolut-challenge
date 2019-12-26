package org.szederz.banking;

import org.szederz.banking.local.LocalBank;
import org.szederz.banking.local.account.LocalAccount;
import org.szederz.banking.local.account.currency.LocalCurrency;
import org.szederz.banking.local.account.identifier.AccountNumber;

public class BankTestHelper {
  public static final AccountId ACCOUNT_NUMBER_1 =
    new AccountNumber(12345678, 12345678);
  public static final AccountId ACCOUNT_NUMBER_2 =
    new AccountNumber(87654321, 87654321);

  public LocalBank localBank = new LocalBank();

  public void registerAccount(AccountId accountId, int balance) {
    localBank.put(new LocalAccount(accountId, new LocalCurrency(balance)));
  }

  public LocalAccount getAccount(AccountId accountId) {
    return localBank.getLocal(accountId)
      .orElseThrow(() -> new AssertionError(
        String.format("Could not find account with id %s", accountId)));
  }

  public boolean hasAccount(AccountId accountId) {
    return localBank.get(accountId).isPresent();
  }
}
