package org.szederz.banking.components.display.web.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.szederz.banking.Account;
import org.szederz.banking.AccountId;
import org.szederz.banking.interactor.read.ReadAccountInteractor;
import org.szederz.banking.interactor.read.ReadAccountRequest;
import org.szederz.banking.interactor.read.ReadAccountResponse;

import java.io.IOException;
import java.util.Optional;

import static org.szederz.banking.components.display.web.HttpStatusCode.NOT_FOUND;
import static org.szederz.banking.components.display.web.HttpStatusCode.OK;
import static org.szederz.banking.components.display.web.handlers.HttpAccountParsers.parseAccountId;
import static org.szederz.banking.components.display.web.handlers.ResponseHelper.send;
import static org.szederz.banking.components.display.web.handlers.ResponseHelper.sendSerializedAccount;
import static org.szederz.banking.interactor.ResponseCode.TRANSACTION_APPROVED;

public class ReadAccountHttpHandler implements HttpHandler {
  private final ReadAccountInteractor interactor;
  private final String prefix;

  public ReadAccountHttpHandler(String prefix, ReadAccountInteractor interactor) {
    this.interactor = interactor;
    this.prefix = prefix;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    Optional<AccountId> optional = parseAccountId(
      exchange.getRequestURI().getPath().replaceFirst(prefix + "/", ""));

    if(!optional.isPresent()) {
      send(exchange, NOT_FOUND);
      return;
    }

    AccountId accountId = optional.get();


    ReadAccountResponse response = interactor.readAccount(
      new ReadAccountRequest()
        .withAccountId(accountId));

    sendResponse(exchange, response);
  }

  private void sendResponse(HttpExchange exchange, ReadAccountResponse response) throws IOException {
    if(response.getCode() == TRANSACTION_APPROVED) {
      sendOKResponse(exchange, response);
      return;
    }

    send(exchange, response.getCode());
  }

  private void sendOKResponse(HttpExchange exchange, ReadAccountResponse response) throws IOException {
    Optional<Account> optional = response.getAccount();
    if(optional.isPresent()) {
      sendSerializedAccount(exchange, OK, optional.get());
    }
  }
}
