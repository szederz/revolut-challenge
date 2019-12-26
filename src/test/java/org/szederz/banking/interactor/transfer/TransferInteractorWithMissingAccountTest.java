package org.szederz.banking.interactor.transfer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.szederz.banking.interactor.ResponseCode.NO_CREDIT_ACCOUNT;
import static org.szederz.banking.interactor.transfer.TransferTransactionTestHelper.DONOR_ACCOUNT_NUMBER;
import static org.szederz.banking.interactor.transfer.TransferTransactionTestHelper.RECIPIENT_ACCOUNT_NUMBER;

class TransferInteractorWithMissingAccountTest {
  private TransferTransactionTestHelper helper = new TransferTransactionTestHelper();

  @BeforeEach
  void setUp() {
    helper.request
      .fromAccount(DONOR_ACCOUNT_NUMBER)
      .toAccount(RECIPIENT_ACCOUNT_NUMBER);
  }

  @Test
  void respondsWithNoAccountIfBankHoldsNone() {
    TransferResponse response = helper.transfer();

    assertEquals(NO_CREDIT_ACCOUNT, response.getCode());
  }

  @Test
  void respondsWithInsufficientFundsWhenWrongAccountIsReferenced() {
    helper.registerAccount(DONOR_ACCOUNT_NUMBER, 0);

    TransferResponse response = helper.transfer();

    assertEquals(NO_CREDIT_ACCOUNT, response.getCode());
  }

  @Test
  void respondsWithNoCreditAccountIfRecipientIsMissing() {
    helper.registerAccount(RECIPIENT_ACCOUNT_NUMBER, 0);

    TransferResponse response = helper.transfer();

    assertEquals(NO_CREDIT_ACCOUNT, response.getCode());
  }
}