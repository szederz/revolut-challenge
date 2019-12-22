package org.szederz.banking.transaction.transfer;

import org.szederz.banking.Bank;
import org.szederz.banking.transaction.TransactionRequest;
import org.szederz.banking.transaction.TransactionResponse;
import org.szederz.banking.transaction.TransferTransaction;

public class TransferTransactionTestHelper {
  public Bank bank = new Bank();
  public TransferTransaction transaction = new TransferTransaction(bank);
  public TransactionRequest request = new TransactionRequest();

  public TransactionResponse transfer() {
    return transaction.transfer(request);
  }
}
