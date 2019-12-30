package org.szederz.banking.components.local.account.identifier;

import org.szederz.banking.AccountId;

import java.util.Optional;

public class AccountNumber implements AccountId {
  private final int part1;
  private final int part2;
  private final int part3;

  public static Optional<AccountNumber> parse(String account) {
    if(AccountNumber.isValid(account))
      return Optional.of(AccountNumber.fromString(account));

    return Optional.empty();
  }

  public static boolean isValid(String account) {
    return account.matches("\\d{8}(-\\d{8}){1,2}");
  }

  public static AccountNumber fromString(String accountAsString) {
    String[] accountParts = accountAsString.split("-");

    if(accountParts.length == 2)
      return new AccountNumber(
        Integer.parseInt(accountParts[0]),
        Integer.parseInt(accountParts[1]));

    return new AccountNumber(
      Integer.parseInt(accountParts[0]),
      Integer.parseInt(accountParts[1]),
      Integer.parseInt(accountParts[2]));
  }

  public AccountNumber(int part1, int part2, int part3) {
    this.part1 = part1;
    this.part2 = part2;
    this.part3 = part3;
  }

  public AccountNumber(int part1, int part2) {
    this(part1, part2, 0);
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;

    AccountNumber that = (AccountNumber) o;

    if(part1 != that.part1) return false;
    if(part2 != that.part2) return false;
    return part3 == that.part3;
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode();
  }

  @Override
  public String toString() {
    return String.format("%08d-%08d-%08d", part1, part2, part3);
  }
}
