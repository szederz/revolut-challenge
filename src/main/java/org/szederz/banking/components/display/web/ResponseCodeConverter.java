package org.szederz.banking.components.display.web;

import org.szederz.banking.interactor.ResponseCode;

import java.util.HashMap;
import java.util.Map;

import static org.szederz.banking.components.display.web.HttpStatusCode.*;
import static org.szederz.banking.interactor.ResponseCode.*;

public class ResponseCodeConverter {
  private static final Map<ResponseCode, HttpStatusCode> CONVERT_MAP = new HashMap<>();

  static {
    CONVERT_MAP.put(TRANSACTION_APPROVED, OK);
    CONVERT_MAP.put(REENTER_LAST_TRANSACTION, PRECONDITION_FAILED);
    CONVERT_MAP.put(NO_CREDIT_ACCOUNT, NOT_FOUND);
    CONVERT_MAP.put(INSUFFICIENT_FUNDS, BAD_REQUEST);
    CONVERT_MAP.put(DUPLICATE_TRANSACTION, CONFLICT);
  }

  public static HttpStatusCode asHttpStatusCode(ResponseCode code) {
    return CONVERT_MAP.getOrDefault(code, INTERNAL_SERVER_ERROR);
  }
}
