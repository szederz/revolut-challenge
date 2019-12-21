package org.szederz.banking;

import org.szederz.banking.account.AccountNumber;
import org.szederz.banking.account.BankAccount;
import org.szederz.banking.currency.Currency;
import org.szederz.banking.response.AccountResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.szederz.banking.response.AccountResponse.*;

public class Bank {
  private Map<AccountNumber, BankAccount> accounts = new HashMap<>();

  public BankResponse transfer(Transaction transaction) {
    Optional<BankAccount> optionalDonor = getAccount(transaction.getDonorAccountNumber());
    Optional<BankAccount> optionalRecipient = getAccount(transaction.getRecipientAccountNumber());

    if (optionalDonor.isPresent() && optionalRecipient.isPresent()) {
      return new BankResponse(
        transfer(optionalDonor.get(), transaction, optionalRecipient.get()));
    }

    return new BankResponse(NO_CREDIT_ACCOUNT);


  }

  private AccountResponse transfer(BankAccount donorAccount, Transaction transaction, BankAccount recipientAccount) {
    Currency amountToTransfer = transaction.getAmount();

    if (!donorAccount.hasAtLeast(amountToTransfer)) {
      return INSUFFICIENT_FUNDS;
    }

    this.withAccount(donorAccount.withdraw(amountToTransfer));
    this.withAccount(recipientAccount.deposit(amountToTransfer));

    return TRANSACTION_APPROVED;
  }

  public Bank withAccount(BankAccount existingBankAccount) {
    this.accounts.put(existingBankAccount.getAccountNumber(), existingBankAccount);
    return this;
  }

  public Optional<BankAccount> getAccount(AccountNumber accountNumber) {
    if (accounts.containsKey(accountNumber)) {
      return Optional.of(accounts.get(accountNumber));
    }
    return Optional.empty();
  }
}
