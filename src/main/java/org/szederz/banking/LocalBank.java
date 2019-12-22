package org.szederz.banking;

import org.szederz.banking.transaction.TransactionCode;

import java.util.*;

import static org.szederz.banking.transaction.TransactionCode.TRANSACTION_APPROVED;

public class LocalBank<ACCOUNT extends Account & Versionable> implements Bank<ACCOUNT> {
  private Map<AccountIdentifier, ACCOUNT> accounts = new HashMap<>();


  @Override
  public void saveAccounts(ACCOUNT... accounts) {
    for (ACCOUNT account : accounts) {
      saveAccount(account);
    }
  }

  @Override
  public TransactionCode saveAccount(ACCOUNT account) {
    this.accounts.put(account.getAccountId(), account);
    return TRANSACTION_APPROVED;
  }


  @Override
  public Optional<List<ACCOUNT>> getAccounts(AccountIdentifier... identifiers) {
    List<ACCOUNT> accounts = new ArrayList<>(identifiers.length);

    for(AccountIdentifier identifier : identifiers) {
      Optional<ACCOUNT> optionalAccount = getAccount(identifier);

      if (!optionalAccount.isPresent())
        return Optional.empty();

      accounts.add(optionalAccount.get());
    }

    return Optional.of(accounts);
  }

  @Override
  public Optional<ACCOUNT> getAccount(AccountIdentifier identifier) {
    if (accounts.containsKey(identifier)) {
      return Optional.of(accounts.get(identifier));
    }
    return Optional.empty();
  }
}
