package org.szederz.banking.interactor.close;

import org.szederz.banking.interactor.Response;
import org.szederz.banking.interactor.ResponseCode;

public class CloseAccountResponse extends Response {
  public CloseAccountResponse(ResponseCode code) {
    super(code);
  }
}
