package mikaa.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class StringFilterTest {

  private static final String STRING = "abcdef";

  @ParameterizedTest
  @ValueSource(strings = { "a", "b", "cd", "def" })
  public void shouldContainString(String s) {
    var filter = new StringFilter(s);
    assertTrue(filter.contains(STRING));
  }

  @ParameterizedTest
  @NullAndEmptySource
  public void emptyFilterShouldContainAnyString(String empty) {
    var emptyFilter = new StringFilter(empty);
    assertTrue(emptyFilter.contains("xyz"));
  }

  @ParameterizedTest
  @ValueSource(strings = { "g", "acb", "aa" })
  public void shouldNotContainString(String s) {
    var filter = new StringFilter(s);
    assertFalse(filter.contains(STRING));
  }

}
