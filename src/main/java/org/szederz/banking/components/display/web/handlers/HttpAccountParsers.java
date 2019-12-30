package org.szederz.banking.components.display.web.handlers;

import org.szederz.banking.AccountId;
import org.szederz.banking.components.local.account.identifier.AccountNumber;

import java.util.Optional;

public class HttpAccountParsers {
  public static Optional<AccountId> parseAccountId(String account) {
    if(AccountNumber.isValid(account))
      return Optional.of(AccountNumber.fromString(account));

    return Optional.empty();
  }
}
