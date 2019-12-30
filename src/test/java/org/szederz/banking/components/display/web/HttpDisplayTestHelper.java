package org.szederz.banking.components.display.web;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.szederz.banking.components.display.web.HttpMethod.*;
import static org.szederz.banking.components.entries.HttpLocalAccountEntry.configureDisplay;

class HttpDisplayTestHelper {
  public HttpDisplay server = configureDisplay(18088);
  public HttpURLConnection connection;

  public void connect(String endpoint) throws IOException {
    URL url = new URL("http://localhost:" + server.getPort() + endpoint);
    connection = (HttpURLConnection) url.openConnection();
  }

  public void delete(String account, String version, ThrowingConsumer<HttpURLConnection> assertions) throws Exception {
    connect("/account/" + account);
    usingMethod(
      DELETE,
      conn -> {
        conn.addRequestProperty("If-Match", "\"" + version + "\"");
      },
      assertions
    );
  }

  public void read(String account, ThrowingConsumer<HttpURLConnection> assertions) throws Exception {
    connect("/account/" + account);
    usingMethod(GET, assertions);
  }

  public void create(
    String account,
    ThrowingConsumer<HttpURLConnection> assertions
  ) throws Exception {
    connect("/account");
    usingMethod(
      POST,
      conn -> {
        conn.setDoOutput(true);
        conn.getOutputStream().write(account.getBytes(UTF_8));
      },
      assertions);
  }

  public void update(
    String account,
    String update,
    ThrowingConsumer<HttpURLConnection> assertions
  ) throws Exception {
    connect("/account/" + account);
    usingMethod(
      PUT,
      conn -> {
        conn.setDoOutput(true);
        conn.getOutputStream().write(update.getBytes(UTF_8));
      },
      assertions);
  }

  public void transfer(
    String account1,
    long amount,
    String account2,
    ThrowingConsumer<HttpURLConnection> assertions
  ) throws Exception {
    connect("/transfer");
    usingMethod(
      PUT,
      conn -> {
        conn.setDoOutput(true);
        conn.getOutputStream().write(("{" +
          "\"from\": \"" + account1 + "\"," +
          "\"to\": \"" + account2 + "\"," +
          "\"amount\": " + amount +
          "}").getBytes(UTF_8));
      },
      assertions);
  }

  public void usingMethod(
    HttpMethod method,
    ThrowingConsumer<HttpURLConnection> assertions
  ) throws Exception {
    usingMethod(
      method,
      (conn) -> {
      },
      assertions);
  }

  public void usingMethod(
    HttpMethod method,
    ThrowingConsumer<HttpURLConnection> setter,
    ThrowingConsumer<HttpURLConnection> assertions
  ) throws Exception {
    connection.setRequestMethod(method.name());
    setter.accept(connection);

    connection.connect();
    connection.getResponseCode();
    assertions.accept(connection);
    connection.disconnect();
  }

  public interface ThrowingConsumer<T> {
    void accept(T t) throws Exception;
  }
}