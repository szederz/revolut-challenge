package org.szederz.banking.interactor.close;

import org.szederz.banking.Account;
import org.szederz.banking.interactor.open.OpenAccountRequest;
import org.szederz.banking.local.BankTestHelper;
import org.szederz.banking.local.account.LocalAccount;

public class CloseAccountInteractorTestHelper extends BankTestHelper {
  public CloseAccountInteractor interactor = new CloseAccountInteractor(bank);
  public CloseAccountRequest request = new CloseAccountRequest();

  public CloseAccountResponse closeAccount() {
    return interactor.closeAccount(request);
  }

  private Account createAccountFrom(OpenAccountRequest request) {
    return new LocalAccount(
      request.getAccountId(),
      request.getBalance());
  }
}
