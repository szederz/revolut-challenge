package org.szederz.banking.components.local;

import org.szederz.banking.Account;
import org.szederz.banking.AccountId;
import org.szederz.banking.Bank;
import org.szederz.banking.components.local.account.LocalAccount;
import org.szederz.banking.interactor.ResponseCode;
import org.szederz.collections.Pair;
import org.szederz.concurrency.FunctionalLock;

import java.util.*;
import java.util.stream.Collectors;

import static org.szederz.banking.interactor.ResponseCode.*;
import static org.szederz.collections.Pair.zip;

public class LocalBank implements Bank {
  private final FunctionalLock lock = new FunctionalLock();
  private final Map<AccountId, LocalAccount> accounts = new HashMap<>();

  @Override
  public ResponseCode createAll(List<Account> accounts) {
    Map<AccountId, LocalAccount> accountMap = createMapFrom(
      asLocalAccounts(accounts));

    return lock.forWrite(() -> createAll(accountMap));
  }

  @Override
  public Optional<List<Account>> getAll(List<AccountId> ids) {
    return getAllLocal(ids)
      .map(l -> new ArrayList<>(l));
  }

  @Override
  public ResponseCode updateAll(List<Account> accounts) {
    List<LocalAccount> accountsToUpdate = asLocalAccounts(accounts);
    return lock.forWrite(() -> updateAllLocal(accountsToUpdate));
  }

  @Override
  public ResponseCode remove(Account accountToRemove) {
    return lock.forWrite(() ->
      getLocal(accountToRemove.getAccountId())
        .map(savedAccount ->
          this.removeExistingAccount(accountToRemove, savedAccount))
        .orElse(NO_CREDIT_ACCOUNT));
  }

  private ResponseCode createAll(Map<AccountId, LocalAccount> accountMap) {
    for(AccountId id : accountMap.keySet())
      if(this.accounts.containsKey(id))
        return DUPLICATE_TRANSACTION;

    this.accounts.putAll(accountMap);

    return TRANSACTION_APPROVED;
  }

  private Optional<List<LocalAccount>> getAllLocal(List<AccountId> ids) {
    List<LocalAccount> accounts = new ArrayList<>(ids.size());

    for(AccountId id : ids) {
      Optional<LocalAccount> optionalAccount = getLocal(id);

      if(!optionalAccount.isPresent())
        return Optional.empty();

      accounts.add(optionalAccount.get());
    }

    return Optional.of(accounts);
  }

  private ResponseCode updateAllLocal(List<LocalAccount> accountsToUpdate) {
    Optional<List<LocalAccount>> savedAccounts = getAllLocal(getIdsOf(accountsToUpdate));

    if(!savedAccounts.isPresent())
      return NO_CREDIT_ACCOUNT;

    if(isAnyStale(zip(accountsToUpdate, savedAccounts.get())))
      return REENTER_LAST_TRANSACTION;

    this.accounts.putAll(
      createMapFrom(
        allWithIncreasedVersion(accountsToUpdate)));

    return TRANSACTION_APPROVED;
  }

  private ResponseCode removeExistingAccount(Account accountToRemove, LocalAccount savedAccount) {
    if(accountToRemove.hasDifferentVersionFrom(savedAccount)) {
      return REENTER_LAST_TRANSACTION;
    }

    accounts.remove(accountToRemove.getAccountId());

    return TRANSACTION_APPROVED;
  }

  public Optional<LocalAccount> getLocal(AccountId identifier) {
    LocalAccount account;

    account = lock.forRead(() -> accounts.get(identifier));

    if(account == null)
      return Optional.empty();

    return Optional.of(account);
  }

  private Map<AccountId, LocalAccount> createMapFrom(List<LocalAccount> accounts) {
    Map<AccountId, LocalAccount> localAccounts = new HashMap<>();

    for(Account account : accounts) {
      localAccounts.put(account.getAccountId(), asLocalAccount(account));
    }

    return localAccounts;
  }

  private List<LocalAccount> allWithIncreasedVersion(List<LocalAccount> accountToUpdate) {
    List<LocalAccount> versionUpdated = new ArrayList<>();
    for(LocalAccount account : accountToUpdate) {
      versionUpdated.add(account.withVersion(account.getVersion() + 1));
    }
    return versionUpdated;
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

  private boolean isAnyStale(
    List<Pair<LocalAccount, LocalAccount>> newAndOldAccounts
  ) {
    return newAndOldAccounts.stream()
      .anyMatch(pair ->
        pair.getLeft().hasDifferentVersionFrom(pair.getRight()));
  }

  private List<AccountId> getIdsOf(List<? extends Account> accounts) {
    return accounts.stream()
      .map(Account::getAccountId)
      .collect(Collectors.toList());
  }
}
