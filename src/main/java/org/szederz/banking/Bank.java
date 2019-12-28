package org.szederz.banking;

import org.szederz.banking.interactor.ResponseCode;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

public interface Bank {
  default ResponseCode create(Account account) {
    return createAll(singletonList(account));
  }

  ResponseCode createAll(List<Account> singletonList);

  default ResponseCode update(Account account) {
    return updateAll(singletonList(account));
  }

  ResponseCode updateAll(List<Account> accounts);

  default Optional<Account> get(AccountId identifier) {
    return getAll(singletonList(identifier))
      .map(accounts -> accounts.get(0));
  }

  Optional<List<Account>> getAll(List<AccountId> identifiers);

  ResponseCode remove(Account account);
}
