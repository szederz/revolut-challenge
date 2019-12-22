package org.szederz.banking;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class Bank {
  private Map<AccountIdentifier, Account> accounts = new HashMap<>();


  public void saveAccounts(Account... accounts) {
    Stream.of(accounts)
      .forEach(this::saveAccount);
  }

  public void saveAccount(Account account) {
    this.accounts.put(account.getAccountIdentifier(), account);
  }

  public Optional<Account> getAccount(AccountIdentifier identifier) {
    if (accounts.containsKey(identifier)) {
      return Optional.of(accounts.get(identifier));
    }
    return Optional.empty();
  }
}
