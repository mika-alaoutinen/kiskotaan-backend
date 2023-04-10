package mikaa.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RangeFilterTest {

  private static final RangeFilter<Integer> RANGE = new RangeFilter<Integer>(1, 3);

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
    var range = new RangeFilter<Integer>(null, 3);
    assertTrue(range.within(3));
  }

  @Test
  void shouldHandleNullMaxValue() {
    var range = new RangeFilter<Integer>(1, null);
    assertTrue(range.within(1));
  }

  @Test
  void shouldAlwaysBeWithinOpenRange() {
    var openRange = new RangeFilter<Integer>(null, null);
    assertTrue(openRange.within(1));
  }

}
