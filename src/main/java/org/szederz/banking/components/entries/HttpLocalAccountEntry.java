package org.szederz.banking.components.entries;

import org.szederz.banking.Account;
import org.szederz.banking.components.display.web.HttpDisplay;
import org.szederz.banking.components.display.web.RESTHttpHandler;
import org.szederz.banking.components.display.web.handlers.*;
import org.szederz.banking.components.local.LocalBank;
import org.szederz.banking.components.local.account.LocalAccount;
import org.szederz.banking.interactor.close.CloseAccountInteractor;
import org.szederz.banking.interactor.open.OpenAccountInteractor;
import org.szederz.banking.interactor.open.OpenAccountRequest;
import org.szederz.banking.interactor.read.ReadAccountInteractor;
import org.szederz.banking.interactor.transfer.TransferInteractor;
import org.szederz.banking.interactor.update.UpdateBalanceInteractor;

public class HttpLocalAccountEntry {
  public static void main(String... args) {
    HttpDisplay display = configureDisplay(Integer.parseInt(args[0]));
    display.start();
  }

  public static HttpDisplay configureDisplay(int port) {
    LocalBank bank = new LocalBank();
    String prefix = "/account";

    return new HttpDisplay(port)
      .withEndpoint(prefix, new RESTHttpHandler()
        .forCreate(
          new OpenAccountHttpHandler(
            new OpenAccountInteractor(bank, HttpLocalAccountEntry::accountFrom)))
        .forRead(
          new ReadAccountHttpHandler(prefix, new ReadAccountInteractor(bank)))
        .forUpdate(
          new UpdateBalanceHttpHandler(prefix, new UpdateBalanceInteractor(bank)))
        .forDelete(
          new CloseAccountHttpHandler(prefix, new CloseAccountInteractor(bank))))
      .withEndpoint("/transfer", new TransferHttpHandler(new TransferInteractor(bank)));
  }

  private static Account accountFrom(OpenAccountRequest request) {
    return new LocalAccount(request.getAccountId(), request.getBalance());
  }
}
