package org.szederz.banking.transaction.transfer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.szederz.banking.Bank;
import org.szederz.banking.account.identifier.AccountNumber;
import org.szederz.banking.account.LocalAccount;
import org.szederz.banking.transaction.TransactionRequest;
import org.szederz.banking.transaction.TransactionResponse;
import org.szederz.banking.transaction.TransferTransaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.szederz.banking.transaction.TransactionCode.NO_CREDIT_ACCOUNT;

class MissingAccountTest {
  public static final AccountNumber DONOR_ACCOUNT_NUMBER =
    new AccountNumber(12345678, 12345678);
  public static final AccountNumber RECIPIENT_ACCOUNT_NUMBER =
    new AccountNumber(87654321, 87654321);

  private Bank bank;
  private TransactionRequest transactionRequest;
  private TransferTransaction interactor;

  @BeforeEach
  void setUp() {
    bank = new Bank();
    interactor = new TransferTransaction(bank);
    transactionRequest = new TransactionRequest()
      .fromAccountNumber(DONOR_ACCOUNT_NUMBER)
      .toAccountNumber(RECIPIENT_ACCOUNT_NUMBER);
  }

  @Test
  void respondsWithNoAccountIfBankHoldsNone() {
    TransactionResponse response = transfer();

    assertEquals(NO_CREDIT_ACCOUNT, response.getCode());
  }

  @Test
  void respondsWithInsufficientFundsWhenWrongAccountIsReferenced() {
    bank.saveAccount(new LocalAccount(RECIPIENT_ACCOUNT_NUMBER));

    TransactionResponse response = transfer();

    assertEquals(NO_CREDIT_ACCOUNT, response.getCode());
  }

  @Test
  void respondsWithNoCreditAccountIfRecipientIsMissing() {
    bank.saveAccount(new LocalAccount(DONOR_ACCOUNT_NUMBER));

    TransactionResponse response = transfer();

    assertEquals(NO_CREDIT_ACCOUNT, response.getCode());
  }

  private TransactionResponse transfer() {
    return interactor.transfer(transactionRequest);
  }
}