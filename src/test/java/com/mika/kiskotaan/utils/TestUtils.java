package com.mika.kiskotaan.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.mika.kiskotaan.models.Course;
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

    public List<Course> parseModels(MvcResult result, Class<? extends EntityModel> model) throws Exception {
        CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, model);
        return mapper.readValue(parseResponseString(result), collectionType);
    }

    public Course parseModel(MvcResult result, Class<? extends EntityModel> model) throws Exception {
        JavaType type = mapper.getTypeFactory().constructType(model);
        return mapper.readValue(parseResponseString(result), type);
    }

    public String parseResponseString(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public String writeModel(Object model) throws JsonProcessingException {
        return mapper.writeValueAsString(model);
    }
}
