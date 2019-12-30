package org.szederz.banking.components.display.web.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.szederz.banking.components.local.account.currency.LocalCurrency;
import org.szederz.banking.components.local.account.identifier.AccountNumber;
import org.szederz.banking.interactor.transfer.TransferInteractor;
import org.szederz.banking.interactor.transfer.TransferRequest;
import org.szederz.banking.interactor.transfer.TransferResponse;

import java.io.IOException;
import java.util.Optional;

import static org.szederz.banking.components.display.web.HttpMethod.PUT;
import static org.szederz.banking.components.display.web.HttpStatusCode.BAD_REQUEST;
import static org.szederz.banking.components.display.web.HttpStatusCode.METHOD_NOT_ALLOWED;
import static org.szederz.banking.components.display.web.handlers.ResponseHelper.send;
import static org.szederz.banking.components.display.web.json.JsonParser.parseJson;

public class TransferHttpHandler implements HttpHandler {
  private final TransferInteractor interactor;

  public TransferHttpHandler(TransferInteractor interactor) {
    this.interactor = interactor;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if(!exchange.getRequestMethod().equals(PUT.name())) {
      send(exchange, METHOD_NOT_ALLOWED);
      return;
    }

    Optional<TransferRequest> optional =
      parseJson(exchange.getRequestBody())
        .filter(this::valid)
        .map(this::createRequestFrom);

    if(!optional.isPresent()) {
      send(exchange, BAD_REQUEST);
      return;
    }

    TransferResponse response = interactor.transfer(optional.get());

    send(exchange, response.getCode());
  }

  private boolean valid(JsonNode requestBody) {
    return requestBody.hasNonNull("from")
      && AccountNumber.isValid(requestBody.get("from").asText())
      && requestBody.hasNonNull("to")
      && AccountNumber.isValid(requestBody.get("to").asText())
      && requestBody.hasNonNull("amount")
      && requestBody.get("amount").isNumber();
  }

  private TransferRequest createRequestFrom(JsonNode json) {
    return new TransferRequest()
      .ofAmount(new LocalCurrency(json.get("amount").asLong()))
      .fromAccount(AccountNumber.fromString(json.get("from").asText()))
      .toAccount(AccountNumber.fromString(json.get("to").asText()));
  }
}
