package org.szederz.banking.local.account.currency;

import org.szederz.banking.Currency;

public class LocalCurrency implements Currency {
  private final long amount;

  public LocalCurrency(long amount) {
    this.amount = amount;
  }

  public long getAmount() {
    return this.amount;
  }

  @Override
  public Currency plus(Currency currency) {
    return new LocalCurrency(this.amount + currency.getAmount());
  }

  @Override
  public Currency minus(Currency currency) {
    return new LocalCurrency(this.amount - currency.getAmount());
  }

  @Override
  public boolean isLessThan(Currency currency) {
    return amount < currency.getAmount();
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o instanceof Currency) return this.equals((LocalCurrency) o);

    return false;
  }

  @Override
  public boolean equals(Currency currency) {
    return amount == currency.getAmount();
  }
}
