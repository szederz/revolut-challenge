package org.szederz.banking.interactor;

public class Response {
  private final ResponseCode code;

  public Response(ResponseCode code) {
    this.code = code;
  }

  public ResponseCode getCode() {
    return this.code;
  }
}
