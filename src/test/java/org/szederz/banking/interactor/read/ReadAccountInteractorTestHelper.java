package org.szederz.banking.interactor.read;

import org.szederz.banking.components.local.BankTestHelper;

public class ReadAccountInteractorTestHelper extends BankTestHelper {
  public ReadAccountInteractor interactor = new ReadAccountInteractor(bank);
  public ReadAccountRequest request = new ReadAccountRequest();

  public ReadAccountResponse readAccount() {
    return interactor.readAccount(request);
  }
}
