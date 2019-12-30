package org.szederz.banking.components.display.web.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.szederz.banking.Account;
import org.szederz.banking.components.local.account.currency.LocalCurrency;
import org.szederz.banking.components.local.account.identifier.AccountNumber;
import org.szederz.banking.interactor.open.OpenAccountInteractor;
import org.szederz.banking.interactor.open.OpenAccountRequest;
import org.szederz.banking.interactor.open.OpenAccountResponse;

import java.io.IOException;
import java.util.Optional;

import static org.szederz.banking.components.display.web.HttpStatusCode.BAD_REQUEST;
import static org.szederz.banking.components.display.web.HttpStatusCode.CREATED;
import static org.szederz.banking.components.display.web.handlers.ResponseHelper.send;
import static org.szederz.banking.components.display.web.handlers.ResponseHelper.sendSerializedAccount;
import static org.szederz.banking.components.display.web.json.JsonParser.parseJson;
import static org.szederz.banking.interactor.ResponseCode.TRANSACTION_APPROVED;

public class OpenAccountHttpHandler implements HttpHandler {
  private final OpenAccountInteractor interactor;

  public OpenAccountHttpHandler(OpenAccountInteractor interactor) {
    this.interactor = interactor;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    Optional<OpenAccountRequest> optional =
      parseJson(exchange.getRequestBody())
        .filter(this::valid)
        .map(this::createRequestFrom);

    if(!optional.isPresent()) {
      send(exchange, BAD_REQUEST);
      return;
    }

    OpenAccountResponse response = interactor.openAccount(optional.get());

    sendHttpResponse(exchange, response);
  }

  private boolean valid(JsonNode requestBody) {
    return !requestBody.toString().equals("")
      && requestBody.isObject()
      && requestBody.hasNonNull("account")
      && requestBody.get("account").isTextual()
      && AccountNumber.isValid(requestBody.get("account").asText())
      && requestBody.hasNonNull("balance")
      && requestBody.get("balance").isNumber();
  }

  private OpenAccountRequest createRequestFrom(JsonNode json) {
    return new OpenAccountRequest()
      .ofAmount(
        new LocalCurrency(json.get("balance").asLong()))
      .withAccountId(
        AccountNumber.fromString(json.get("account").asText()));
  }

  private void sendHttpResponse(HttpExchange exchange, OpenAccountResponse response) throws IOException {
    if(response.getCode() == TRANSACTION_APPROVED) {
      sendCreatedResponse(exchange, response);
      return;
    }
    send(exchange, response.getCode());
  }

  private void sendCreatedResponse(HttpExchange exchange, OpenAccountResponse response) throws IOException {
    Optional<Account> optional = response.getAccount();
    if(optional.isPresent()) {
      sendSerializedAccount(exchange, CREATED, optional.get());
    }
  }
}
