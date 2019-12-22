package org.szederz.banking.transaction;

public class TransactionResponse {
  private final TransactionCode responseCode;

  public TransactionResponse(TransactionCode responseCode) {
    this.responseCode = responseCode;
  }

  public TransactionCode getCode() {
    return this.responseCode;
  }
}
