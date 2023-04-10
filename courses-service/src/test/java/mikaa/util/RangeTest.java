package mikaa.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RangeTest {

  private static final Range<Integer> RANGE = new Range<Integer>(1, 3);

  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3 })
  void shouldBeWithinRangeInclusive(int i) {
    assertTrue(RANGE.within(i));
  }

  @ParameterizedTest
  @ValueSource(ints = { 0, 4 })
  void shouldNotBeInRange(int i) {
    assertFalse(RANGE.within(i));
  }

  @Test
  void shouldHandleNullMinValue() {
    var range = new Range<Integer>(null, 3);
    assertTrue(range.within(3));
  }

  @Test
  void shouldHandleNullMaxValue() {
    var range = new Range<Integer>(1, null);
    assertTrue(range.within(1));
  }

  @Test
  void shouldAlwaysBeWithinOpenRange() {
    var openRange = new Range<Integer>(null, null);
    assertTrue(openRange.within(1));
  }

}
