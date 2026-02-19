package com.thejatech.whatsapp.controller;

import com.thejatech.whatsapp.model.FormSubmission;
import com.thejatech.whatsapp.service.FormProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class FormController {

    private final FormProcessorService formProcessorService;

    @Autowired
    public FormController(FormProcessorService formProcessorService) {
        this.formProcessorService = formProcessorService;
    }

    @PostMapping("/submit")
    public ResponseEntity<String> handleSubmission(@RequestBody FormSubmission submission) {
        if (submission == null) {
            return ResponseEntity.badRequest().body("Invalid submission data");
        }
        
        formProcessorService.processSubmission(submission);
        return ResponseEntity.ok("Submission received and processed successfully!");
    }
}
