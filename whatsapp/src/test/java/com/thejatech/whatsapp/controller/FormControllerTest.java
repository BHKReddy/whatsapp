package com.thejatech.whatsapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thejatech.whatsapp.model.FormSubmission;
import com.thejatech.whatsapp.service.FormProcessorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FormController.class)
class FormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FormProcessorService formProcessorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testHandleSubmission() throws Exception {
        // Arrange
        FormSubmission submission = new FormSubmission();
        submission.setName("Test User");
        submission.setEmail("test@example.com");
        submission.setPhone("1234567890");
        submission.setAddress("123 Main St");
        submission.setMandal("MyMandal");
        submission.setDistrict("MyDistrict");
        submission.setState("MyState");
        submission.setPincode("500001");
        submission.setFacingProblems("No Water");

        // Act & Assert
        mockMvc.perform(post("/webhook/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submission)))
                .andExpect(status().isOk());

        // Verify the mock service was called
        verify(formProcessorService).processSubmission(any(FormSubmission.class));
    }
}
