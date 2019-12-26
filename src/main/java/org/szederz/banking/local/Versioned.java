package org.szederz.banking.local;

public interface Versioned {
  long getVersion();

  Versioned withIncreasedVersion();
}
