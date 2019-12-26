package org.szederz.banking.local;

import org.szederz.banking.Account;
import org.szederz.banking.AccountId;
import org.szederz.banking.Bank;
import org.szederz.banking.local.account.LocalAccount;
import org.szederz.banking.collections.Pair;
import org.szederz.banking.interactor.ResponseCode;

import java.util.*;
import java.util.stream.Collectors;

import static org.szederz.banking.collections.Pair.zip;
import static org.szederz.banking.interactor.ResponseCode.REENTER_LAST_TRANSACTION;
import static org.szederz.banking.interactor.ResponseCode.TRANSACTION_APPROVED;

public class LocalBank implements Bank {
  private final Object mutex = new Object();
  private final Map<AccountId, LocalAccount> accounts = new HashMap<>();

  @Override
  public ResponseCode putAll(List<Account> accounts) {
    synchronized(mutex) {
      List<Pair<LocalAccount, Optional<LocalAccount>>> updatedAndAccounts =
        zip(asLocalAccounts(accounts), getAny(getIdsOf(accounts)));

      if(isAnyStale(updatedAndAccounts)) {
        return REENTER_LAST_TRANSACTION;
      }

      this.accounts.putAll(accountUpdatesFrom(updatedAndAccounts));

      return TRANSACTION_APPROVED;
    }
  }

  @Override
  public Optional<List<Account>> getAll(List<AccountId> ids) {
    List<Account> accounts = new ArrayList<>(ids.size());

    for(AccountId id : ids) {
      Optional<LocalAccount> optionalAccount = getLocal(id);

      if(!optionalAccount.isPresent())
        return Optional.empty();

      accounts.add(optionalAccount.get());
    }

    return Optional.of(accounts);
  }

  private List<Optional<LocalAccount>> getAny(List<AccountId> ids) {
    List<Optional<LocalAccount>> optionalAccounts = new ArrayList<>(ids.size());

    for(AccountId id : ids) {
      optionalAccounts.add(getLocal(id));
    }

    return optionalAccounts;
  }

  public Optional<LocalAccount> getLocal(AccountId identifier) {
    if(accounts.containsKey(identifier)) {
      return Optional.of(accounts.get(identifier));
    }
    return Optional.empty();
  }

  private List<LocalAccount> asLocalAccounts(List<Account> accounts) {
    return accounts.stream()
      .map(this::asLocalAccount)
      .collect(Collectors.toList());
  }

  private LocalAccount asLocalAccount(Account account) {
    if(account instanceof LocalAccount) {
      return (LocalAccount) account;
    }
    return new LocalAccount(account.getAccountId(), account.getBalance());
  }

  private Map<AccountId, LocalAccount> accountUpdatesFrom(
    List<Pair<LocalAccount, Optional<LocalAccount>>> newAndOldAccounts
  ) {
    Map<AccountId, LocalAccount> accountUpdates =
      new HashMap<>(newAndOldAccounts.size());

    newAndOldAccounts.forEach(newAndOldAccount -> {
      LocalAccount account = accountUpdateFrom(newAndOldAccount);
      accountUpdates.put(account.getAccountId(), account);
    });

    return accountUpdates;
  }

  private LocalAccount accountUpdateFrom(
    Pair<LocalAccount, Optional<LocalAccount>> newAndOldAccount
  ) {
    LocalAccount account = asLocalAccount(newAndOldAccount.getLeft());

    if(newAndOldAccount.getRight().isPresent())
      return account.withIncreasedVersion();

    return account;
  }

  private boolean isAnyStale(
    List<Pair<LocalAccount, Optional<LocalAccount>>> newAndOldAccounts
  ) {
    return newAndOldAccounts.stream()
      .anyMatch(this::isStale);
  }

  private boolean isStale(
    Pair<LocalAccount, Optional<LocalAccount>> newAndOldAccount
  ) {
    return newAndOldAccount.getRight()
      .filter(oldAccount ->
        newAndOldAccount.getLeft().getVersion() != oldAccount.getVersion())
      .isPresent();
  }

  private List<AccountId> getIdsOf(List<Account> accounts) {
    return accounts.stream()
      .map(Account::getAccountId)
      .collect(Collectors.toList());
  }
}
