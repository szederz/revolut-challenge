package org.szederz.banking.interactor.transfer;

import org.szederz.banking.Account;
import org.szederz.banking.Bank;
import org.szederz.banking.Currency;
import org.szederz.banking.interactor.ResponseCode;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.szederz.banking.interactor.ResponseCode.*;

public class TransferInteractor {
  private final Bank bank;

  public TransferInteractor(Bank bank) {
    this.bank = bank;
  }

  public TransferResponse transfer(TransferRequest transferRequest) {
    return getDonorAndRecipientAccounts(transferRequest)
      .map(accounts ->
        new TransferResponse(
          transfer(
            accounts.get(0),
            transferRequest.getAmount(),
            accounts.get(1))))
      .orElse(
        new TransferResponse(NO_CREDIT_ACCOUNT));
  }

  private Optional<List<Account>> getDonorAndRecipientAccounts(TransferRequest transferRequest) {
    return bank.getAll(asList(
      transferRequest.getDonorAccountId(),
      transferRequest.getRecipientAccountId()));
  }

  private ResponseCode transfer(Account donor, Currency amountToTransfer, Account recipient) {
    if(donor.getBalance().isLessThan(amountToTransfer)) {
      return INSUFFICIENT_FUNDS;
    }

    return bank.putAll(asList(
      donor.withdraw(amountToTransfer),
      recipient.deposit(amountToTransfer)
    ));
  }
}
