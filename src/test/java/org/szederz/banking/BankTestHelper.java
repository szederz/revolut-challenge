package org.szederz.banking;

import org.szederz.banking.account.LocalAccount;
import org.szederz.banking.account.identifier.AccountNumber;
import org.szederz.banking.currency.LocalCurrency;

public class BankTestHelper {
  public static final AccountIdentifier ACCOUNT_NUMBER_1 =
    new AccountNumber(12345678, 12345678);
  public static final AccountIdentifier ACCOUNT_NUMBER_2 =
    new AccountNumber(87654321, 87654321);

  public LocalBank<LocalAccount> localBank = new LocalBank<>();

  public void registerAccount(AccountIdentifier accountId, int balance) {
    localBank.saveAccount(new LocalAccount(accountId, new LocalCurrency(balance)));
  }

  public LocalAccount getAccount(AccountIdentifier accountId) {
    return localBank.getAccount(accountId)
      .orElseThrow(AssertionError::new);
  }

  public boolean hasAccount(AccountIdentifier accountId) {
    return localBank.getAccount(accountId).isPresent();
  }
}
