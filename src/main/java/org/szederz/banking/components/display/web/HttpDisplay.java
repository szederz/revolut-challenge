package org.szederz.banking.components.display.web;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpDisplay {
  private final int port;
  private HttpServer server;

  public HttpDisplay(int port) {
    try {
      this.port = port;
      server = HttpServer.create(new InetSocketAddress(port), 0);
    } catch(IOException e) {
      throw new HttpInitializationException(e);
    }
  }

  public HttpDisplay withEndpoint(String endpoint, HttpHandler handler) {
    HttpContext context = server.createContext(endpoint);
    context.setHandler(handler);
    return this;
  }

  public void start() {
    server.start();
  }

  public void stop() {
    server.stop(0);
  }

  public int getPort() {
    return port;
  }
}
