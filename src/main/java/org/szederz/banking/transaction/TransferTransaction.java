package org.szederz.banking.transaction;

import org.szederz.banking.Account;
import org.szederz.banking.LocalBank;
import org.szederz.banking.currency.Currency;

import java.util.List;
import java.util.Optional;

import static org.szederz.banking.transaction.TransactionCode.*;

public class TransferTransaction {
  private final LocalBank localBank;

  public TransferTransaction(LocalBank localBank) {
    this.localBank = localBank;
  }

  public TransactionResponse transfer(TransactionRequest transactionRequest) {
    return getDonorAndRecipientAccounts(transactionRequest)
      .map(accounts ->
        new TransactionResponse(
          transfer(
            accounts.get(0),
            transactionRequest.getAmount(),
            accounts.get(1))))
      .orElse(
        new TransactionResponse(NO_CREDIT_ACCOUNT));
  }

  private Optional<List<Account>> getDonorAndRecipientAccounts(TransactionRequest transactionRequest) {
    return localBank.getAccounts(
      transactionRequest.getDonorAccountId(),
      transactionRequest.getRecipientAccountId());
  }

  private TransactionCode transfer(Account donor, Currency amountToTransfer, Account recipient) {
    if (donor.getBalance().isLessThan(amountToTransfer)) {
      return INSUFFICIENT_FUNDS;
    }

    localBank.saveAccounts(
      donor.withdraw(amountToTransfer),
      recipient.deposit(amountToTransfer)
    );

    return TRANSACTION_APPROVED;
  }
}
