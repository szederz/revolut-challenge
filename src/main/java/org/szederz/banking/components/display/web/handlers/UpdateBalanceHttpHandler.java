package org.szederz.banking.components.display.web.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.szederz.banking.Account;
import org.szederz.banking.AccountId;
import org.szederz.banking.components.local.account.currency.LocalCurrency;
import org.szederz.banking.interactor.update.UpdateBalanceInteractor;
import org.szederz.banking.interactor.update.UpdateBalanceRequest;
import org.szederz.banking.interactor.update.UpdateBalanceResponse;

import java.io.IOException;
import java.util.Optional;

import static org.szederz.banking.components.display.web.HttpStatusCode.*;
import static org.szederz.banking.components.display.web.handlers.HttpAccountParsers.parseAccountId;
import static org.szederz.banking.components.display.web.handlers.ResponseHelper.send;
import static org.szederz.banking.components.display.web.handlers.ResponseHelper.sendSerializedAccount;
import static org.szederz.banking.components.display.web.json.JsonParser.parseJson;
import static org.szederz.banking.interactor.ResponseCode.TRANSACTION_APPROVED;

public class UpdateBalanceHttpHandler implements HttpHandler {
  private final UpdateBalanceInteractor interactor;
  private final String prefix;

  public UpdateBalanceHttpHandler(String prefix, UpdateBalanceInteractor interactor) {
    this.prefix = prefix;
    this.interactor = interactor;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    Optional<AccountId> accountId = parseAccountId(
      exchange.getRequestURI().getPath().replaceFirst(prefix + "/", ""));
    if(!accountId.isPresent()) {
      send(exchange, NOT_FOUND);
      return;
    }

    Optional<UpdateBalanceRequest> optional =
      parseJson(exchange.getRequestBody())
        .filter(this::valid)
        .map(this::createRequestFrom);

    if(!optional.isPresent()) {
      send(exchange, BAD_REQUEST);
      return;
    }

    UpdateBalanceResponse response = interactor.updateBalance(optional.get()
      .withAccountId(accountId.get()));

    sendHttpResponse(exchange, response);
  }

  private boolean valid(JsonNode requestBody) {
    return (
      requestBody.hasNonNull("deposit")
        && requestBody.get("deposit").isNumber()
    ) || (
      requestBody.hasNonNull("withdraw")
        && requestBody.get("withdraw").isNumber()
    );
  }

  private UpdateBalanceRequest createRequestFrom(JsonNode json) {
    UpdateBalanceRequest request = new UpdateBalanceRequest();

    if(json.hasNonNull("deposit")) {
      request.deposit(new LocalCurrency(json.get("deposit").asLong()));
    }
    if(json.hasNonNull("withdraw")) {
      request.withdraw(new LocalCurrency(json.get("withdraw").asLong()));
    }

    return request;
  }

  private void sendHttpResponse(HttpExchange exchange, UpdateBalanceResponse response) throws IOException {
    if(response.getCode() == TRANSACTION_APPROVED) {
      sendOkResponse(exchange, response);
      return;
    }
    send(exchange, response.getCode());
  }

  private void sendOkResponse(HttpExchange exchange, UpdateBalanceResponse response) throws IOException {
    Optional<Account> optional = response.getAccount();
    if(optional.isPresent()) {
      sendSerializedAccount(exchange, OK, optional.get());
    }
    send(exchange, OK);
  }
}
