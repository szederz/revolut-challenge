package org.szederz.banking.interactor.transfer;

import org.szederz.banking.interactor.ResponseCode;

public class TransferResponse {
  private final ResponseCode responseCode;

  public TransferResponse(ResponseCode responseCode) {
    this.responseCode = responseCode;
  }

  public ResponseCode getCode() {
    return this.responseCode;
  }
}
