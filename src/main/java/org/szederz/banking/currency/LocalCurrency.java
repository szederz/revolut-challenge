package org.szederz.banking.currency;

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
}
