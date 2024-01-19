package io.reflectoring.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reflectoring.api.BatchApiController;
import io.reflectoring.model.BatchMessage;
import io.reflectoring.model.MessageParameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BatchApiControllerTest {


    @Autowired(required = false)
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BatchApiController batchApiController;

    @Test
    void sendBatchMessage400() throws Exception {

        BatchMessage batchMessage = new BatchMessage();
        batchMessage.setChannelAccount("");

        MessageParameters messageParameters = new MessageParameters();
        messageParameters.setSignature("");

        mockMvc.perform(post("/sms/batch")
                .param("tenantId", "tenantId")
                .content(objectMapper.writeValueAsString(batchMessage))
                .contentType("application/json"))
                .andExpect(status().isBadRequest());

    }
}