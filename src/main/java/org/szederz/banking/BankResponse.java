package org.szederz.banking;

import org.szederz.banking.response.AccountResponse;

public class BankResponse {
  private final AccountResponse responseCode;

  public BankResponse(AccountResponse responseCode) {
    this.responseCode = responseCode;
  }

  public AccountResponse getCode() {
    return this.responseCode;
  }
}
