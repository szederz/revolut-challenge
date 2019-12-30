package org.szederz.banking.components.display.web;

public enum HttpStatusCode {
  OK(200),
  CREATED(201),
  NO_CONTENT(204),
  BAD_REQUEST(400),
  METHOD_NOT_ALLOWED(405),
  NOT_FOUND(404),
  CONFLICT(409),
  PRECONDITION_FAILED(412),
  PRECONDITION_REQUIRED(428),
  INTERNAL_SERVER_ERROR(500);

  private final int code;

  HttpStatusCode(int code) {
    this.code = code;
  }

  public int value() {
    return code;
  }
}
