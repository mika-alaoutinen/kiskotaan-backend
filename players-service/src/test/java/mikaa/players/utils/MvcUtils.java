package mikaa.players.utils;

import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public interface MvcUtils {

  static final ObjectMapper MAPPER = new ObjectMapper();

  static <T> String asJson(T dto) throws JsonProcessingException {
    return MAPPER.writeValueAsString(dto);
  }

  static void verifyName(ResultActions result, String firstName, String lastName) throws Exception {
    result
        .andExpect(jsonPath("$.firstName").value(firstName))
        .andExpect(jsonPath("$.lastName").value(lastName));
  }

}
