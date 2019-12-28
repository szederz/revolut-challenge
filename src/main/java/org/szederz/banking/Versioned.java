package org.szederz.banking;

public interface Versioned {
  long getVersion();

  Versioned withVersion(long version);

  default boolean hasDifferentVersionFrom(Versioned versioned) {
    return this.getVersion() != versioned.getVersion();
  }
}
