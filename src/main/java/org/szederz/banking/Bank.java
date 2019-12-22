package org.szederz.banking;

import org.szederz.banking.transaction.TransactionCode;

import java.util.List;
import java.util.Optional;

public interface Bank<ACCOUNT extends Account> {
  void saveAccounts(ACCOUNT... accounts);

  TransactionCode saveAccount(ACCOUNT account);

  Optional<List<ACCOUNT>> getAccounts(AccountIdentifier... identifiers);

  Optional<ACCOUNT> getAccount(AccountIdentifier identifier);
}
