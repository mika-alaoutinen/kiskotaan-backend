package com.mika.kiskotaan.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mika.kiskotaan.utils.TestUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper mapper;
    @Autowired protected TestUtils testUtils;

    public MvcResult performGet(String url) throws Exception {
        return mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    public MvcResult performPost(String url, Object resource) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.writeModel(resource)))
                .andExpect(status().isCreated())
                .andReturn();
    }

    public MvcResult performPut(String url, Object resource) throws Exception {
        return mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.writeModel(resource)))
                .andExpect(status().isOk())
                .andReturn();
    }

    public void performDelete(String url) throws Exception {
        mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
