package org.szederz.banking.account;

public class AccountNumber {
  private final int part1;
  private final int part2;
  private final int part3;

  public AccountNumber(int part1, int part2) {
    this(part1, part2, 0);
  }

  public AccountNumber(int part1, int part2, int part3) {
    this.part1 = part1;
    this.part2 = part2;
    this.part3 = part3;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AccountNumber that = (AccountNumber) o;

    if (part1 != that.part1) return false;
    if (part2 != that.part2) return false;
    return part3 == that.part3;
  }

  @Override
  public int hashCode() {
    int result = part1;
    result = 31 * result + part2;
    result = 31 * result + part3;
    return result;
  }
}
