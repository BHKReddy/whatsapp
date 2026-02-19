package com.thejatech.whatsapp.service.impl;

import com.thejatech.whatsapp.model.FormSubmission;
import com.thejatech.whatsapp.repository.SubmissionRepository;
import com.thejatech.whatsapp.service.CouponService;
import com.thejatech.whatsapp.model.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CouponServiceImpl implements CouponService {

    private final SubmissionRepository submissionRepository;

    @Autowired
    public CouponServiceImpl(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @Override
    public String generateAndSaveCoupon(FormSubmission formSubmission) {
        // Generate a simple unique coupon
        // Taking first 3 chars of name + first 5 chars of UUID
        String base = (formSubmission.getName() != null && !formSubmission.getName().isEmpty()) 
                      ? formSubmission.getName().replaceAll("\\s+", "").toUpperCase() 
                      : "USR";
        if (base.length() > 3) base = base.substring(0, 3);
        
        String uniquePart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String couponCode = base + "T-T" + uniquePart;


        // Correctly mapping fields from FormSubmission (DTO) to Submission (Entity)
        Submission submission = new Submission();
        submission.setName(formSubmission.getName());
        submission.setEmail(formSubmission.getEmail());
        submission.setPhone(formSubmission.getPhone());
        submission.setAddress(formSubmission.getAddress());
        submission.setMandal(formSubmission.getMandal());
        submission.setDistrict(formSubmission.getDistrict());
        submission.setState(formSubmission.getState());
        submission.setPincode(formSubmission.getPincode());
        submission.setFacingProblems(formSubmission.getFacingProblems());
        
        submission.setCouponCode(couponCode);
        submission.setTimestamp(LocalDateTime.now());

        try {
            submissionRepository.save(submission);
            System.out.println("Generated coupon " + couponCode + " and saved submission to MongoDB.");
        } catch (Exception e) {
            System.err.println("FAILED to save to MongoDB: " + e.getMessage());
        }
        
        return couponCode;
    }
}
