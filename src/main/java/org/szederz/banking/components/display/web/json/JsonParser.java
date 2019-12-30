package org.szederz.banking.components.display.web.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class JsonParser {
  private static ObjectMapper MAPPER = new ObjectMapper();

  public static Optional<JsonNode> parseJson(InputStream stream) {
    try {
      JsonNode json = MAPPER.readTree(stream);
      if(json == null) {
        return Optional.empty();
      }

      return Optional.of(json);
    } catch(IOException e) {
      return Optional.empty();
    }
  }

  public static String asJsonString(Object o) {
    return MAPPER.valueToTree(o).toString();
  }
}
