package org.szederz.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class FunctionalLock {
  private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

  public <T> T forWrite(Supplier<T> supplier) {
    Lock writeLock = readWriteLock.writeLock();
    writeLock.lock();
    try {
      return supplier.get();
    } finally {
      writeLock.unlock();
    }
  }

  public <T> T forRead(Supplier<T> supplier) {
    Lock readLock = readWriteLock.writeLock();
    readLock.lock();
    try {
      return supplier.get();
    } finally {
      readLock.unlock();
    }
  }
}
