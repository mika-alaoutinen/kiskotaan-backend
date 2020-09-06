package com.mika.kiskotaan.mappers;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapperUtilsTest {

    @Test
    public void shouldMapIds() {
        var mappedToLong = MapperUtils.mapIds(createIds());
        var expected = List.of(11L, 22L, 33L);
        assertEquals(expected, mappedToLong);
    }

    private Collection<BigDecimal> createIds() {
        return List.of(
                new BigDecimal(11),
                new BigDecimal(22),
                new BigDecimal(33)
        );
    }
}
