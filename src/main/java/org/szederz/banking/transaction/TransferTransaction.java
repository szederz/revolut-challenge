package org.szederz.banking.transaction;

import org.szederz.banking.Account;
import org.szederz.banking.Bank;
import org.szederz.banking.currency.Currency;

import java.util.Optional;

import static org.szederz.banking.transaction.TransactionCode.*;

public class TransferTransaction {
  private final Bank bank;

  public TransferTransaction(Bank bank) {
    this.bank = bank;
  }

  public TransactionResponse transfer(TransactionRequest transactionRequest) {
    Optional<Account> optionalDonor = bank.getAccount(transactionRequest.getDonorAccountNumber());
    Optional<Account> optionalRecipient = bank.getAccount(transactionRequest.getRecipientAccountNumber());

    if (optionalDonor.isPresent() && optionalRecipient.isPresent()) {
      return new TransactionResponse(
        transfer(
          optionalDonor.get(),
          transactionRequest.getAmount(),
          optionalRecipient.get()));
    }

    return new TransactionResponse(NO_CREDIT_ACCOUNT);
  }

  private TransactionCode transfer(Account donor, Currency amountToTransfer, Account recipient) {
    if (donor.getBalance().isLessThan(amountToTransfer)) {
      return INSUFFICIENT_FUNDS;
    }

    bank.saveAccounts(
      donor.withdraw(amountToTransfer),
      recipient.deposit(amountToTransfer)
    );

    return TRANSACTION_APPROVED;
  }
}
