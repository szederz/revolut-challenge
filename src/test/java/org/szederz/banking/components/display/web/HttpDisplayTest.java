package org.szederz.banking.components.display.web;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.szederz.banking.components.display.web.HttpMethod.*;
import static org.szederz.banking.components.display.web.json.JsonParser.parseJson;

class HttpDisplayTest {
  private HttpDisplayTestHelper helper = new HttpDisplayTestHelper();

  @BeforeEach
  void setUp() {
    helper.server.start();
  }

  @AfterEach
  void tearDown() {
    helper.server.stop();
  }

  @Test
  void shouldNotAllowUnknownMethod() throws Exception {
    helper.connect("/account");
    helper.usingMethod(TRACE, (conn) -> {
      assertEquals(405, conn.getResponseCode());
    });
  }

  @Test
  void shouldBeBadRequestWhenBodyIsMissing() throws Exception {
    helper.connect("/account");
    helper.usingMethod(POST, conn -> {
      assertEquals(400, conn.getResponseCode());
    });
  }

  @Test
  void shouldBeBadRequests() throws Exception {
    helper.create("{{{{}", conn -> {
      assertEquals(400, conn.getResponseCode());
    });
    helper.create("{}", conn -> {
      assertEquals(400, conn.getResponseCode());
    });
    helper.create("{\"balance\": 0}", conn -> {
      assertEquals(400, conn.getResponseCode());
    });
    helper.create("{\"account\": \"12345678-12345678\"}", conn -> {
      assertEquals(400, conn.getResponseCode());
    });
    helper.create(
      "{" +
        "\"account\": \"12345678-12345678\"," +
        "\"balance\": \"invalid\"" +
        "}",
      conn -> {
        assertEquals(400, conn.getResponseCode());
      });
    helper.create(
      "{" +
        "\"account\": \"invalid\"," +
        "\"balance\": 0" +
        "}",
      conn -> {
        assertEquals(400, conn.getResponseCode());
      });
  }

  @Test
  void shouldBeNotFoundWhenGettingMissingAccount() throws Exception {
    helper.read("00000000-00000000", (conn) -> {
      assertEquals(404, conn.getResponseCode());
    });
  }

  @Test
  void shouldBeNotFoundWhenAccountNumberIsMalformed() throws Exception {
    helper.read("malformed", (conn) -> {
      assertEquals(404, conn.getResponseCode());
    });
  }

  @Test
  void shouldBeCreatedWhenAccountIsCreated() throws Exception {
    helper.create(
      "{" +
        "\"account\": \"12345678-12345678\"," +
        "\"balance\": 0" +
        "}",
      conn -> {
        assertEquals(201, conn.getResponseCode());
        assertEquals("\"0\"", conn.getHeaderField("ETag"));
      });

    helper.read("12345678-12345678", conn -> {
      assertEquals(200, conn.getResponseCode());
      assertEquals("\"0\"", conn.getHeaderField("ETag"));
      assertTrue(conn.getDoInput());
      JsonNode responseBody = readResponse(conn);
      assertEquals(0, responseBody.get("balance").asLong());
      assertEquals("12345678-12345678-00000000", responseBody.get("account").asText());
    });
  }

  @Test
  void shouldCreateLongFormAccounts() throws Exception {
    helper.create(
      "{" +
        "\"account\": \"12345678-12345678-12345678\"," +
        "\"balance\": 10" +
        "}",
      conn -> {
        assertEquals(201, conn.getResponseCode());
        assertEquals("\"0\"", conn.getHeaderField("ETag"));
        JsonNode responseBody = readResponse(conn);
        assertEquals("12345678-12345678-12345678", responseBody.get("account").asText());
        assertEquals(10, responseBody.get("balance").asLong());
      });

    helper.read("12345678-12345678-12345678", conn -> {
      assertEquals(200, conn.getResponseCode());
    });
  }

  @Test
  void shouldBeConflictWhenAccountIsCreatedTwice() throws Exception {
    String account = "{" +
      "\"account\": \"12345678-12345678\"," +
      "\"balance\": 0" +
      "}";

    helper.create(account, conn -> {
      assertEquals(201, conn.getResponseCode());
    });

    helper.create(account, conn -> {
      assertEquals(409, conn.getResponseCode());
    });
  }

  @Test
  void shouldNotAcceptWithoutIfMatchHeader() throws Exception {
    helper.connect("/account/12345678-12345678");
    helper.usingMethod(
      DELETE,
      conn -> {
        assertEquals(428, conn.getResponseCode());
      }
    );
  }

  @Test
  void shouldNotFindInvalidAccountNumbers() throws Exception {
    helper.delete("invalid", "0", conn -> {
      assertEquals(404, conn.getResponseCode());
    });
  }

  @Test
  void shouldNotDeleteNonExistingAccounts() throws Exception {
    helper.delete("12345678-12345678", "0", conn -> {
      assertEquals(404, conn.getResponseCode());
    });
  }

  @Test
  void shouldDeleteExistingAccounts() throws Exception {
    createExampleAccount();

    helper.delete("12345678-12345678", "0", conn -> {
      assertEquals(204, conn.getResponseCode());
    });
  }

  @Test
  void shouldNotDeleteExistingAccountsWithWrongVersion() throws Exception {
    createExampleAccount();

    helper.delete("12345678-12345678", "2", conn -> {
      assertEquals(412, conn.getResponseCode());
    });
  }

  @Test
  void shouldNotDeleteExistingAccountsWithInvalidVersion() throws Exception {
    createExampleAccount();

    helper.delete("12345678-12345678", "invalid", conn -> {
      assertEquals(400, conn.getResponseCode());
    });
  }

  @Test
  void shouldNotBeAbleToUpdateMissingAccount() throws Exception {
    helper.update("12345678-12345678", "{\"deposit\": 1}", conn -> {
      assertEquals(404, conn.getResponseCode());
    });
  }

  @Test
  void shouldBeBadUpdateRequest() throws Exception {
    helper.update("12345678-12345678", "{\"deposit\": \"invalid\"}", conn -> {
      assertEquals(400, conn.getResponseCode());
    });
  }

  @Test
  void shouldNotFoundInvalidAccount() throws Exception {
    helper.update("invalid", "{\"deposit\": 10}", conn -> {
      assertEquals(404, conn.getResponseCode());
    });
  }

  @Test
  void shouldBeAbleToDepositToAccount() throws Exception {
    createExampleAccount();

    helper.update("12345678-12345678", "{\"deposit\": 1}", conn -> {
      assertEquals(200, conn.getResponseCode());
      assertEquals("\"1\"", conn.getHeaderField("ETag"));
      JsonNode responseBody = readResponse(conn);
      assertEquals("12345678-12345678-00000000", responseBody.get("account").asText());
      assertEquals(1, responseBody.get("balance").asLong());
    });
  }

  @Test
  void shouldBeAbleToWithdrawFromAccount() throws Exception {
    createExampleAccount();

    helper.update("12345678-12345678", "{\"deposit\": 1}", conn -> {
    });

    helper.update("12345678-12345678", "{\"withdraw\": 1}", conn -> {
      assertEquals(200, conn.getResponseCode());
      JsonNode responseBody = readResponse(conn);
      assertEquals(0, responseBody.get("balance").asLong());
    });
  }

  @Test
  void shouldNotBeAbleToCreateDeficit() throws Exception {
    createExampleAccount();

    helper.update("12345678-12345678", "{\"withdraw\": 1}", conn -> {
      assertEquals(400, conn.getResponseCode());
    });
  }

  @Test
  void shouldNotBeAbleToProvideWithDrawAndDeposit() throws Exception {
    createExampleAccount();

    helper.update("12345678-12345678", "{\"deposit\":2,\"withdraw\":1}", conn -> {
      assertEquals(200, conn.getResponseCode());
      JsonNode responseBody = readResponse(conn);
      assertEquals(1, responseBody.get("balance").asLong());
    });
  }

  @Test
  void shouldNotBeAbleToUseOtherMethodsOnTransfer() throws Exception {
    helper.connect("/transfer");
    helper.usingMethod(TRACE, conn -> {
      assertEquals(405, conn.getResponseCode());
    });
  }

  @Test
  void shouldBeBadTransferRequest() throws Exception {
    helper.connect("/transfer");
    helper.usingMethod(PUT, conn -> {
      assertEquals(400, conn.getResponseCode());
    });
  }

  @Test
  void shouldNotBeAbleToTransferFromNonExistingAccount() throws Exception {
    createAccount("87654321-87654321", 0);

    helper.transfer("12345678-12345678", 10, "87654321-87654321", conn -> {
      assertEquals(404, conn.getResponseCode());
    });
  }

  @Test
  void shouldNotBeAbleToTransferToNonExistingAccount() throws Exception {
    createAccount("12345678-12345678", 0);
    helper.transfer("12345678-12345678", 10, "87654321-87654321", conn -> {
      assertEquals(404, conn.getResponseCode());
    });
  }

  @Test
  void shouldBeAbleToTransfer() throws Exception {
    createAccount("12345678-12345678", 10);
    createAccount("87654321-87654321", 0);

    helper.transfer("12345678-12345678", 10, "87654321-87654321", conn -> {
      assertEquals(200, conn.getResponseCode());
    });

    helper.read("12345678-12345678", conn -> {
      JsonNode responseBody = readResponse(conn);
      assertEquals(0, responseBody.get("balance").asLong());
    });

    helper.read("87654321-87654321", conn -> {
      JsonNode responseBody = readResponse(conn);
      assertEquals(10, responseBody.get("balance").asLong());
    });
  }

  @Test
  void shouldNotBeAbleToTransferWhenBalanceIsInsufficient() throws Exception {
    createAccount("12345678-12345678", 0);
    createAccount("87654321-87654321", 0);

    helper.transfer("12345678-12345678", 10, "87654321-87654321", conn -> {
      assertEquals(400, conn.getResponseCode());
    });

    helper.read("12345678-12345678", conn -> {
      JsonNode responseBody = readResponse(conn);
      assertEquals(0, responseBody.get("balance").asLong());
    });

    helper.read("87654321-87654321", conn -> {
      JsonNode responseBody = readResponse(conn);
      assertEquals(0, responseBody.get("balance").asLong());
    });
  }

  private void createExampleAccount() throws Exception {
    createAccount("12345678-12345678", 0);
  }

  private void createAccount(String accountNumber, int balance) throws Exception {
    String account = "{" +
      "\"account\": \"" + accountNumber + "\"," +
      "\"balance\": " + balance +
      "}";

    helper.create(account, conn -> {
      assertEquals(201, conn.getResponseCode());
    });
  }

  private JsonNode readResponse(HttpURLConnection conn) throws IOException {
    Optional<JsonNode> optional = parseJson(conn.getInputStream());

    if(optional.isPresent()) {
      return optional.get();
    }

    throw new AssertionError("Response is not a json object");
  }
}