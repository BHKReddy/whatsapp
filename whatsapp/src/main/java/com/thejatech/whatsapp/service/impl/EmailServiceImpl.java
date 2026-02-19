package com.thejatech.whatsapp.service.impl;

import com.thejatech.whatsapp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String senderEmail;
    private final ResourceLoader resourceLoader;
    private final String bccEmail;
    private final String replyToEmail;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, 
                          @Value("${spring.mail.username}") String senderEmail,
                          @Value("${mail.bcc.address:}") String bccEmail,
                          @Value("${mail.reply.to.address:}") String replyToEmail,
                          ResourceLoader resourceLoader) {
        this.mailSender = mailSender;
        this.senderEmail = senderEmail;
        this.bccEmail = bccEmail;
        this.replyToEmail = replyToEmail;
        this.resourceLoader = resourceLoader;
    }

    @Async
    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(to);
            if (bccEmail != null && !bccEmail.isEmpty()) {
                message.setBcc(bccEmail);
            }
            if (replyToEmail != null && !replyToEmail.isEmpty()) {
                message.setReplyTo(replyToEmail);
            }
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("Email sent (Simple) to " + to + " (BCC: " + bccEmail + ", Reply-To: " + replyToEmail + ")");
        } catch (Exception e) {
            System.err.println("Failed to send simple email to: " + to + ". Error: " + e.getMessage());
        }
    }

    @Async
    @Override
    public void sendEmailWithTemplate(String to, String subject, Map<String, String> placeholders) {
        try {
            // Load template
            Resource resource = resourceLoader.getResource("classpath:email-template.html");
            String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            // Replace placeholders
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                // Using a simple replaceAll (be mindful of special characters, but for this use case it's fine)
                // Escaping the key might be needed if using regex replaceAll, but String.replace (CharSequence) handles literals.
                template = template.replace("${" + entry.getKey() + "}", entry.getValue() != null ? entry.getValue() : "");
            }

            // Create MimeMessage
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(senderEmail);
            helper.setTo(to);
            if (bccEmail != null && !bccEmail.isEmpty()) {
                helper.setBcc(bccEmail);
            }
            if (replyToEmail != null && !replyToEmail.isEmpty()) {
                helper.setReplyTo(replyToEmail);
            }
            helper.setSubject(subject);
            helper.setText(template, true); // true = HTML

            mailSender.send(message);
            System.out.println("Email sent (HTML Template) to " + to + " (BCC: " + bccEmail + ", Reply-To: " + replyToEmail + ")");

        } catch (Exception e) {
            System.err.println("Failed to send HTML email to: " + to + ". Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
