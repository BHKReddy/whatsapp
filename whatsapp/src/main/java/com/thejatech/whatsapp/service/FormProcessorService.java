package com.thejatech.whatsapp.service;

import com.thejatech.whatsapp.model.FormSubmission;

public interface FormProcessorService {
    void processSubmission(FormSubmission submission);
}
