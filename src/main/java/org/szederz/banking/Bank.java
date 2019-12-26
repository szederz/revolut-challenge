package org.szederz.banking;

import org.szederz.banking.interactor.ResponseCode;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

public interface Bank {
  default ResponseCode put(Account account) {
    return putAll(singletonList(account));
  }

  ResponseCode putAll(List<Account> accounts);

  default Optional<Account> get(AccountId identifier) {
    return getAll(singletonList(identifier))
      .map(accounts -> accounts.get(0));
  }

  Optional<List<Account>> getAll(List<AccountId> identifiers);
}
