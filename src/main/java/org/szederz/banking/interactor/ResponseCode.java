package org.szederz.banking.interactor;

public enum ResponseCode {
  TRANSACTION_APPROVED,
  REENTER_LAST_TRANSACTION,
  NO_CREDIT_ACCOUNT,
  INSUFFICIENT_FUNDS,
  DUPLICATE_TRANSACTION
}
