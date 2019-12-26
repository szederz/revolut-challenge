package org.szederz.banking.local.account.identifier;

import org.szederz.banking.AccountId;

import java.util.Random;

public class AccountNumber implements AccountId {
  private final static Random RANDOM = new Random();

  public static AccountNumber random() {
    return new AccountNumber(nextPart(), nextPart(), nextPart());
  }

  private static int nextPart() {
    return RANDOM.nextInt(100000000);
  }

  private final int part1;
  private final int part2;
  private final int part3;

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
