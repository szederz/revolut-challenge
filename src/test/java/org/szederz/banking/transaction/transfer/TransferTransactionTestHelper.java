package org.szederz.banking.transaction.transfer;

import org.szederz.banking.AccountIdentifier;
import org.szederz.banking.BankTestHelper;
import org.szederz.banking.transaction.TransactionRequest;
import org.szederz.banking.transaction.TransactionResponse;
import org.szederz.banking.transaction.TransferTransaction;

public class TransferTransactionTestHelper extends BankTestHelper {
  public static final AccountIdentifier DONOR_ACCOUNT_NUMBER = ACCOUNT_NUMBER_1;
  public static final AccountIdentifier RECIPIENT_ACCOUNT_NUMBER = ACCOUNT_NUMBER_2;

  public TransferTransaction transaction = new TransferTransaction(localBank);
  public TransactionRequest request = new TransactionRequest();

  public TransactionResponse transfer() {
    return transaction.transfer(request);
  }
}
