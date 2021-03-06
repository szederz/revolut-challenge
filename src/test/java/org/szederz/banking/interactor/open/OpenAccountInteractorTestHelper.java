package org.szederz.banking.interactor.open;

import org.szederz.banking.Account;
import org.szederz.banking.components.local.BankTestHelper;
import org.szederz.banking.components.local.account.LocalAccount;

public class OpenAccountInteractorTestHelper extends BankTestHelper {
  public OpenAccountInteractor interactor = new OpenAccountInteractor(bank, this::createAccountFrom);
  public OpenAccountRequest request = new OpenAccountRequest();

  public OpenAccountResponse openAccount() {
    return interactor.openAccount(request);
  }

  private Account createAccountFrom(OpenAccountRequest request) {
    return new LocalAccount(
      request.getAccountId(),
      request.getBalance());
  }
}
