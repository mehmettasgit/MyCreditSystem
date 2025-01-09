package com.creditsystem.integration;
import com.creditsystem.entity.CreditApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreditApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testApplyCredit() throws Exception {
        CreditApplication application = new CreditApplication();
        application.setNationalId("12345678901");
        application.setMonthlyIncome(5000.0);
        application.setCreditScore(600);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/credit/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(application)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creditResult").value("Accept!"));

    }

    @Test
    public void testFindByNationalId() throws Exception {
        String nationalId = "12345678901";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/credit/" + nationalId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nationalId").value(nationalId));
    }
}
