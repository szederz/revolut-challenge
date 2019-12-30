package org.szederz.banking.components.display.web.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.szederz.banking.Account;
import org.szederz.banking.components.display.web.HttpStatusCode;
import org.szederz.banking.interactor.ResponseCode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.szederz.banking.components.display.web.ResponseCodeConverter.asHttpStatusCode;
import static org.szederz.banking.components.display.web.json.JsonParser.asJsonString;

public class ResponseHelper {
  public static void sendSerializedAccount(HttpExchange exchange, HttpStatusCode code, Account account) throws IOException {
    Headers headers = exchange.getResponseHeaders();
    headers.add("ETag", "\"" + account.getVersion() + "\"");

    HashMap<String, Object> responseMap = new HashMap<>();
    responseMap.put("balance", account.getBalance().getAmount());
    responseMap.put("account", account.getAccountId().toString());

    String responseBody = asJsonString(responseMap);
    send(exchange, code, responseBody);
  }

  public static void send(HttpExchange exchange, ResponseCode code) throws IOException {
    Map<String, Object> response = new HashMap<>();
    response.put("code", code.name());
    send(exchange, asHttpStatusCode(code), asJsonString(response));
  }

  public static void send(HttpExchange exchange, HttpStatusCode code) throws IOException {
    send(exchange, code, "");
  }

  public static void send(HttpExchange exchange, HttpStatusCode code, String responseBody) throws IOException {
    byte[] bytes = responseBody.getBytes(UTF_8);
    exchange.sendResponseHeaders(code.value(), bytes.length);
    OutputStream os = exchange.getResponseBody();
    os.write(bytes);
    os.close();
  }
}
