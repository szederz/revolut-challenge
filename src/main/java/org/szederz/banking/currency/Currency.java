package org.szederz.banking.currency;

public interface Currency {
  long getAmount();

  Currency minus(Currency currency);

  Currency plus(Currency currency);

  boolean isLessThan(Currency currency);
}
