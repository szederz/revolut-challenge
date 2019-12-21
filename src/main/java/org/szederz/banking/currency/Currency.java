package org.szederz.banking.currency;

public interface Currency {
  Currency ZERO = new Currency() {
    @Override
    public long getAmount() {
      return 0;
    }

    @Override
    public Currency minus(Currency currency) {
      return ZERO;
    }

    @Override
    public Currency plus(Currency currency) {
      return ZERO;
    }
  };

  long getAmount();

  Currency minus(Currency currency);

  Currency plus(Currency currency);
}
