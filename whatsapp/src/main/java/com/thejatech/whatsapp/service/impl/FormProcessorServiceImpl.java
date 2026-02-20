package com.thejatech.whatsapp.service.impl;

import com.thejatech.whatsapp.model.FormSubmission;
import com.thejatech.whatsapp.service.CouponService;
import com.thejatech.whatsapp.service.EmailService;
import com.thejatech.whatsapp.service.FormProcessorService;
import com.thejatech.whatsapp.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FormProcessorServiceImpl implements FormProcessorService {

    private final EmailService emailService;
    private final WhatsAppService whatsAppService;
    private final CouponService couponService;

    @Value("${exhibition.discount}")
    private String discount;

    @Value("${exhibition.warranty}")
    private String warranty;

    @Value("${exhibition.booth}")
    private String booth;

    @Value("${exhibition.time}")
    private String time;

    @Value("${mail.bcc.address:}")
    private String bccEmail;

    @Autowired
    public FormProcessorServiceImpl(EmailService emailService, WhatsAppService whatsAppService, CouponService couponService) {
        this.emailService = emailService;
        this.whatsAppService = whatsAppService;
        this.couponService = couponService;
    }

    @Override
    public void processSubmission(FormSubmission submission) {
        System.out.println("Processing submission: " + submission);

        // Generate Coupon and Save to DB
        String couponCode = couponService.generateAndSaveCoupon(submission);

        // Process Email
        String targetEmail = submission.getEmail();
        boolean isFallback = false;

        if (targetEmail == null || targetEmail.trim().isEmpty()) {
            if (bccEmail != null && !bccEmail.trim().isEmpty()) {
                System.out.println("User email is missing. Falling back to BCC address: " + bccEmail);
                targetEmail = bccEmail;
                isFallback = true;
            } else {
                System.out.println("User email is missing and BCC address not configured.");
            }
        }

        if (targetEmail != null && !targetEmail.trim().isEmpty()) {
            if (!isFallback) {
                System.out.println("Email found in submission. Attempting to send email to: " + targetEmail);
            }
            
            Map<String, String> emailPlaceholders = new HashMap<>();
            emailPlaceholders.put("name", submission.getName());
            emailPlaceholders.put("registrationCode", couponCode);
            emailPlaceholders.put("phone", submission.getPhone());
            emailPlaceholders.put("email", submission.getEmail() != null ? submission.getEmail() : ""); // Ensure not null for placeholder
            emailPlaceholders.put("address", submission.getAddress());
            emailPlaceholders.put("mandal", submission.getMandal());
            emailPlaceholders.put("district", submission.getDistrict());
            emailPlaceholders.put("state", submission.getState());
            emailPlaceholders.put("pincode", submission.getPincode());
            emailPlaceholders.put("facingProblems", submission.getFacingProblems());

            emailService.sendEmailWithTemplate(
                    targetEmail,
                    "Your Registration Confirmation - Dear: " + submission.getName(),
                    emailPlaceholders
            );
        } else {
             System.out.println("WARNING: Email field is empty or null. Skipping email sending.");
        }

        // Process WhatsApp
        if (submission.getPhone() != null && !submission.getPhone().isEmpty()) {
             System.out.println("Phone found in submission. Attempting to send WhatsApp to: " + submission.getPhone());
             
             String whatsappMessage = String.format(
                "నమస్కారం %s,\n\n" +
                "రిజిస్ట్రేషన్ కోసం ధన్యవాదాలు! 🙏\n\n" +
                "మీ కోడ్: %s\n\n" +
                "ఎగ్జిబిషన్ స్పెషల్:\n" +
                "✅ ₹%s తగ్గింపు\n" +
                "✅ ఫ్రీ ఇన్స్టాలేషన్\n" +
                "✅ %s సంవత్సరాల వారంటీ\n\n" +
                "బూత్ నంబర్: %s\n" +
                "సమయం: %s\n\n" +
                "త్వరలో కలుద్దాం! 😊\n\n" +
                "Hello %s,\n\n" +
                "Thank you for registration! 🙏\n\n" +
                "Your code: %s\n\n" +
                "Exhibition special:\n" +
                "✅ ₹%s discount\n" +
                "✅ FREE installation\n" +
                "✅ %s year warranty\n\n" +
                "See you soon! 😊",
                submission.getName(), couponCode, discount, warranty, booth, time,
                submission.getName(), couponCode, discount, warranty
             );

            whatsAppService.sendWhatsAppMessage(
                    submission.getPhone(),
                    whatsappMessage
            );
        } else {
             System.out.println("WARNING: Phone field is empty or null. Skipping WhatsApp message.");
        }
    }

    private String formatMessage(FormSubmission submission, String couponCode) {
        return "New Submission Details:\n\n" +
                "COUPON CODE: " + couponCode + "\n\n" +
                "Name: " + submission.getName() + "\n" +
                "Phone: " + submission.getPhone() + "\n" +
                "Email: " + submission.getEmail() + "\n" +
                "Address: " + submission.getAddress() + "\n" +
                "Mandal: " + submission.getMandal() + "\n" +
                "District: " + submission.getDistrict() + "\n" +
                "State: " + submission.getState() + "\n" +
                "Pincode: " + submission.getPincode() + "\n" +
                "Problem: " + submission.getFacingProblems();
    }
}
