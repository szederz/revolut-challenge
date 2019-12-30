package org.szederz.banking.components.display.web.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.szederz.banking.AccountId;
import org.szederz.banking.components.local.account.LocalAccount;
import org.szederz.banking.components.local.account.identifier.AccountNumber;
import org.szederz.banking.interactor.ResponseCode;
import org.szederz.banking.interactor.close.CloseAccountInteractor;
import org.szederz.banking.interactor.close.CloseAccountRequest;
import org.szederz.banking.interactor.close.CloseAccountResponse;

import java.io.IOException;
import java.util.Optional;

import static org.szederz.banking.components.display.web.HttpStatusCode.*;
import static org.szederz.banking.components.display.web.handlers.ResponseHelper.send;

public class CloseAccountHttpHandler implements HttpHandler {
  private final CloseAccountInteractor interactor;
  private final String prefix;

  public CloseAccountHttpHandler(String prefix, CloseAccountInteractor interactor) {
    this.interactor = interactor;
    this.prefix = prefix;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    Optional<Long> optionalVersion = parseVersionFrom(exchange.getRequestHeaders());
    if(!optionalVersion.isPresent()) {
      send(exchange, PRECONDITION_REQUIRED);
      return;
    }

    Optional<AccountId> optionalAccountId = parseAccountId(exchange.getRequestURI().getPath());
    if(!optionalAccountId.isPresent()) {
      send(exchange, NOT_FOUND);
      return;
    }

    CloseAccountResponse response = interactor.closeAccount(
      new CloseAccountRequest().withAccount(
        new LocalAccount(optionalAccountId.get())
          .withVersion(optionalVersion.get())));

    sendResponse(exchange, response);
  }

  private void sendResponse(HttpExchange exchange, CloseAccountResponse response) throws IOException {
    if(response.getCode() == ResponseCode.TRANSACTION_APPROVED) {
      send(exchange, NO_CONTENT);
      return;
    }
    send(exchange, response.getCode());
  }

  private Optional<Long> parseVersionFrom(Headers headers) {
    if(!headers.containsKey("If-Match"))
      return Optional.empty();

    return Optional.of(
      Long.parseLong(
        headers.getFirst("If-Match")
          .replaceAll("\"", "")));
  }

  private Optional<AccountId> parseAccountId(String path) {
    String accountNumber = path.replaceFirst(prefix + "/", "");

    if(AccountNumber.isValid(accountNumber))
      return Optional.of(AccountNumber.fromString(accountNumber));

    return Optional.empty();
  }
}
