package org.szederz.banking;

public interface Currency {
  long getAmount();

  Currency minus(Currency currency);

  Currency plus(Currency currency);

  boolean isLessThan(Currency currency);

  boolean equals(Currency currency);
}
