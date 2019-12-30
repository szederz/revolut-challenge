package org.szederz.banking.components.display.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import static org.szederz.banking.components.display.web.HttpMethod.*;
import static org.szederz.banking.components.display.web.HttpStatusCode.METHOD_NOT_ALLOWED;

public class RESTHttpHandler implements HttpHandler {
  private HttpHandler readHandler;
  private HttpHandler createHandler;
  private HttpHandler deleteHandler;
  private HttpHandler updateHandler;

  public RESTHttpHandler() {
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if(isMethodIn(exchange, POST))
      createHandler.handle(exchange);
    else if(isMethodIn(exchange, GET))
      readHandler.handle(exchange);
    else if(isMethodIn(exchange, PUT))
      updateHandler.handle(exchange);
    else if(isMethodIn(exchange, DELETE))
      deleteHandler.handle(exchange);
    else
      exchange.sendResponseHeaders(METHOD_NOT_ALLOWED.value(), 0);
  }

  private boolean isMethodIn(HttpExchange exchange, HttpMethod method) {
    return exchange.getRequestMethod().equals(method.name());
  }

  public RESTHttpHandler forCreate(HttpHandler handler) {
    this.createHandler = handler;
    return this;
  }

  public RESTHttpHandler forRead(HttpHandler readHandler) {
    this.readHandler = readHandler;
    return this;
  }

  public RESTHttpHandler forUpdate(HttpHandler readHandler) {
    this.updateHandler = readHandler;
    return this;
  }

  public RESTHttpHandler forDelete(HttpHandler handler) {
    this.deleteHandler = handler;
    return this;
  }
}
