package net.codejava.utility;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MailSenderUtil {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String FROM;

    public void sendBasicMail(String toEmail, String subject, String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(FROM);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);

        new Thread(() -> mailSender.send(simpleMailMessage)).start();
    }

    public void sendMailWithHTML(String toEmail, String subject, String template, Map<String, Object> variables)
            throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        mimeMessageHelper.setFrom(FROM);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(subject);
        Context context = new Context();
        context.setVariables(variables);
        String html = templateEngine.process(template, context);

        mimeMessageHelper.setText(html, true);

        new Thread(() -> mailSender.send(mimeMessage)).start();
    }
}
