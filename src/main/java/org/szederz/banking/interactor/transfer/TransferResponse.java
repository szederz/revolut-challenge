package org.szederz.banking.interactor.transfer;

import org.szederz.banking.interactor.Response;
import org.szederz.banking.interactor.ResponseCode;

public class TransferResponse extends Response {
  public TransferResponse(ResponseCode code) {
    super(code);
  }
}
