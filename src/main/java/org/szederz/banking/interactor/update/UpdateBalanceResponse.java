package org.szederz.banking.interactor.update;

import org.szederz.banking.interactor.Response;
import org.szederz.banking.interactor.ResponseCode;

public class UpdateBalanceResponse extends Response {
  public UpdateBalanceResponse(ResponseCode code) {
    super(code);
  }
}
