package com.mika.kiskotaan.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.mika.kiskotaan.models.EntityModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestUtils {
    private final ObjectMapper mapper;

    public String parseResponseString(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public String writeModel(Object model) throws JsonProcessingException {
        return mapper.writeValueAsString(model);
    }

    public CollectionType getCollectionType(Class<? extends EntityModel> model) {
        return mapper.getTypeFactory().constructCollectionType(List.class, model);
    }
}
