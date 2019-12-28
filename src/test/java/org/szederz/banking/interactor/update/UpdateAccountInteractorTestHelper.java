package org.szederz.banking.interactor.update;

import org.szederz.banking.Account;
import org.szederz.banking.interactor.open.OpenAccountRequest;
import org.szederz.banking.local.BankTestHelper;
import org.szederz.banking.local.account.LocalAccount;

public class UpdateAccountInteractorTestHelper extends BankTestHelper {
  public UpdateAccountInteractor interactor = new UpdateAccountInteractor(bank);
  public UpdateAccountRequest request = new UpdateAccountRequest();

  public UpdateAccountResponse updateAccount() {
    return interactor.updateAccount(request);
  }

  private Account createAccountFrom(OpenAccountRequest request) {
    return new LocalAccount(
      request.getAccountId(),
      request.getBalance());
  }
}
