package com.thejatech.whatsapp.service;

import com.thejatech.whatsapp.model.FormSubmission;

public interface CouponService {
    String generateAndSaveCoupon(FormSubmission submission);
}
