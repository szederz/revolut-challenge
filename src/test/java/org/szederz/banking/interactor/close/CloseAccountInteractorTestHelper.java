package org.szederz.banking.interactor.close;

import org.szederz.banking.Account;
import org.szederz.banking.components.local.BankTestHelper;
import org.szederz.banking.components.local.account.LocalAccount;
import org.szederz.banking.interactor.open.OpenAccountRequest;

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
