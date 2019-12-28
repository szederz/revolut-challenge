package org.szederz.banking.interactor.read;

import org.szederz.banking.Account;
import org.szederz.banking.interactor.close.CloseAccountInteractor;
import org.szederz.banking.interactor.close.CloseAccountRequest;
import org.szederz.banking.interactor.close.CloseAccountResponse;
import org.szederz.banking.interactor.open.OpenAccountRequest;
import org.szederz.banking.local.BankTestHelper;
import org.szederz.banking.local.account.LocalAccount;

public class ReadAccountInteractorTestHelper extends BankTestHelper {
  public ReadAccountInteractor interactor = new ReadAccountInteractor(bank);
  public ReadAccountRequest request = new ReadAccountRequest();

  public ReadAccountResponse readAccount() {
    return interactor.readAccount(request);
  }
}
