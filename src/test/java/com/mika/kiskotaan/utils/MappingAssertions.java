package com.mika.kiskotaan.utils;

import com.mika.kiskotaan.models.Hole;
import kiskotaan.openapi.model.HoleResource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class MappingAssertions {

    public static void assertHoleMapping(Hole model, HoleResource resource) {
        assertEquals(model.getNumber(), resource.getNumber());
        assertEquals(model.getPar(), resource.getPar());
        assertEquals(model.getDistance(), resource.getDistance());
    }
}
