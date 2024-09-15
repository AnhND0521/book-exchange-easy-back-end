package itss.group22.bookexchangeeasy.service.impl;

import itss.group22.bookexchangeeasy.service.MailService;
import itss.group22.bookexchangeeasy.service.mail.Mail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String selfEmail;

    @Override
    public void sendMail(String to, Mail mail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(selfEmail);
            helper.setTo(to);
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent(), true);

            mailSender.send(message);
            log.info("Mail sent successfully to {}", to);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
