package org.szederz.banking.collections;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class Pair<T, U> {
  private T left;
  private U right;

  public Pair(T left, U right) {
    this.left = left;
    this.right = right;
  }

  public T getLeft() {
    return left;
  }

  public U getRight() {
    return right;
  }

  public static <T, U> List<Pair<T, U>> zip(List<T> lefts, List<U> rights) {
    int size = min(lefts.size(), rights.size());
    List<Pair<T, U>> pairs = new ArrayList<>(size);

    for(int i = 0; i < size; i++) {
      pairs.add(new Pair<>(lefts.get(i), rights.get(i)));
    }

    return pairs;
  }
}
