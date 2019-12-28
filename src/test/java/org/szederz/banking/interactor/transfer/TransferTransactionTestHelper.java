package org.szederz.banking.interactor.transfer;

import org.szederz.banking.AccountId;
import org.szederz.banking.local.BankTestHelper;

public class TransferTransactionTestHelper extends BankTestHelper {
  public static final AccountId DONOR_ACCOUNT_NUMBER = ACCOUNT_NUMBER_1;
  public static final AccountId RECIPIENT_ACCOUNT_NUMBER = ACCOUNT_NUMBER_2;

  public TransferInteractor interactor = new TransferInteractor(bank);
  public TransferRequest request = new TransferRequest();

  public TransferResponse transfer() {
    return interactor.transfer(request);
  }
}
