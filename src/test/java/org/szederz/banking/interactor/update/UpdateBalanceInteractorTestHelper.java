package org.szederz.banking.interactor.update;

import org.szederz.banking.components.local.BankTestHelper;

public class UpdateBalanceInteractorTestHelper extends BankTestHelper {
  public UpdateBalanceInteractor interactor = new UpdateBalanceInteractor(bank);
  public UpdateBalanceRequest request = new UpdateBalanceRequest();

  public UpdateBalanceResponse updateAccount() {
    return interactor.updateBalance(request);
  }
}
