package com.mika.kiskotaan.mappers;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MapperUtils {

    public static List<Long> mapIds(Collection<BigDecimal> ids) {
        return ids.stream()
                .mapToLong(BigDecimal::longValue)
                .sorted()
                .boxed()
                .collect(Collectors.toList());
    }
}
